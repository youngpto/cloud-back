package com.iot.cloudback.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.iot.cloudback.dao.DeviceDao;
import com.iot.cloudback.dao.ForwardTableDao;
import com.iot.cloudback.dao.MqttLogDao;
import com.iot.cloudback.dao.UserDao;
import com.iot.cloudback.entity.*;
import com.iot.cloudback.entity.base.UpdateStreamData;
import com.iot.cloudback.entity.constants.ResultEnum;
import com.iot.cloudback.mqtt.CloudMqttClient;
import org.apache.ibatis.ognl.Ognl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.service
 * @ClassName: DeviceService
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/20 19:13
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/20 19:13
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CloudMqttClient mqttClient;

    @Autowired
    private ForwardTableDao forwardTableDao;

    @Autowired
    private UpDataService upDataService;

    @Autowired
    private MqttLogDao mqttLogDao;

    public Result findAllDeviceById(int userId) {
        try {
            User query = new User();
            query.setId(userId);
            User user = userDao.findUser(query).get(0);
            List<Device> devices = deviceDao.findByUser(user.getName());
            Result result = ResultEnum.FIND_SUCCESS.getResult();
            result.setData(devices);
            return result;
        } catch (Exception e) {
            return ResultEnum.FIND_ERROR.getResult();
        }
    }

    public Result addDevice(Device device, int userId) {
        User query = new User();
        query.setId(userId);
        String userName = userDao.findUser(query).get(0).getName();

        int id;
        List<Device> all = deviceDao.findAll();
        if (CollectionUtil.isEmpty(all)) {
            id = 1;
        } else {
            id = all.get(all.size() - 1).getDeviceId() + 1;
        }

        device.setUserName(userName);
        device.setDeviceId(id);

        String upDataStreamId = "/" + userName + "/" +
                device.getDeviceName() + "/update";

        String subscribeTopic = "/" + userName + "/" +
                device.getDeviceName() + "/get";

        device.setUpDataStreamId(upDataStreamId);
        device.setSubscribeTopic(subscribeTopic);

        try {
            deviceDao.add(device);
            mqttClient.subscribe(upDataStreamId);
            return ResultEnum.ADD_SUCCESS.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultEnum.ADD_ERROR.getResult();
    }

    public Result updateDeviceInfo(Device device) {
        try {
            deviceDao.update(device);
            return ResultEnum.UPDATE_SUCCESS.getResult();
        } catch (Exception e) {
            return ResultEnum.UPDATE_ERROR.getResult();
        }
    }

    public Result delete(int deviceId) {
        try {
            String upDataStreamId = deviceDao.findOne(deviceId).getUpDataStreamId();
            mqttClient.unSubscribe(upDataStreamId);

            deviceDao.delete(deviceId);

            return ResultEnum.DELETE_SUCCESS.getResult();
        } catch (Exception e) {
            return ResultEnum.DELETE_ERROR.getResult();
        }
    }

    public Result findDeviceByUsername(String username) {
        try {
            List<Device> devices = deviceDao.findByUser(username);
            Result result = ResultEnum.FIND_SUCCESS.getResult();
            result.setData(devices);
            return result;
        } catch (Exception e) {
            return ResultEnum.FIND_ERROR.getResult();
        }
    }

    public Result addForwardRule(String sender, String receiver, int userId) {
        User query = new User();
        query.setId(userId);
        String userName = userDao.findUser(query).get(0).getName();

        String senderTopic = "/" + userName + "/" + sender + "/update";
        String receiverTopic = "/" + userName + "/" + receiver + "/get";

        try {
            ForwardTable one = forwardTableDao.findOne(senderTopic);
            if (one == null) {
                List<String> receivers = new ArrayList<>();
                receivers.add(receiverTopic);
                one = new ForwardTable(senderTopic, receivers);
                forwardTableDao.add(one);
            } else {
                List<String> oneReceiver = one.getReceiver();
                for (String s : oneReceiver) {
                    if (s.equals(receiverTopic)) {
                        throw new IllegalArgumentException("重复转发异常");
                    }
                }
                oneReceiver.add(receiverTopic);
                one.setReceiver(oneReceiver);
                forwardTableDao.update(one);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEnum.ADD_ERROR.getResult();
        }
        return ResultEnum.ADD_SUCCESS.getResult();
    }

    public Result findDataByDataType(String deviceName, int dataType, int userId) {
        User query = new User();
        query.setId(userId);
        String userName = userDao.findUser(query).get(0).getName();

        String upDataStreamId = deviceDao.findOne(userName, deviceName).getUpDataStreamId();

        try {
            List<? extends UpdateStreamData> data = upDataService.downloadListByUdsId(dataType, upDataStreamId, 0, 20);
            Result result = ResultEnum.FIND_SUCCESS.getResult();
            result.setData(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEnum.FIND_ERROR.getResult();
        }
    }

    public Result sendMessageWithDevice(String userName, String deviceName, JSONObject message) {
        if (StrUtil.isEmpty(userName)) {
            // 用户与设备信息为空则向全体设备广播数据
            List<Device> all = deviceDao.findAll();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Device device : all) {
                        mqttClient.addLog(3, device.getSubscribeTopic(), message);
                        mqttClient.publish(device.getSubscribeTopic(), 0, message.toString());
                    }
                }
            }).start();
        } else {
            if (StrUtil.isEmpty(deviceName)) {
                // 用户信息不为空，设备信息为空仅向目标用户所有设备发送消息
                List<Device> byUser = deviceDao.findByUser(userName);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (Device device : byUser) {
                            mqttClient.addLog(3, device.getSubscribeTopic(), message);
                            mqttClient.publish(device.getSubscribeTopic(), 0, message.toString());
                        }
                    }
                }).start();
            } else {
                // 两者都不为空则可确定唯一标识设备
                Device one = deviceDao.findOne(userName, deviceName);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mqttClient.addLog(3, one.getSubscribeTopic(), message);
                        mqttClient.publish(one.getSubscribeTopic(), 0, message.toString());
                    }
                }).start();
            }
        }
        return ResultEnum.ADD_SUCCESS.getResult();
    }

    public Result findMqttLog(int logType, int count, String userName, String deviceName) {
        List<MqttLog> mqttLogs;
        boolean uEmpty = StrUtil.isEmpty(userName);
        boolean dEmpty = StrUtil.isEmpty(deviceName);

        if (uEmpty) {
            if (logType == 0) {
                mqttLogs = mqttLogDao.findList(0, count);
            } else {
                mqttLogs = mqttLogDao.findListByLogType(logType, 0, count);
            }
        } else {
            String topic = "^/" + userName;
            if (!dEmpty) {
                topic += "/" + deviceName;
            }
            if (logType == 0) {
                mqttLogs = mqttLogDao.findListByTopic(topic, 0, count);
            } else {
                mqttLogs = mqttLogDao.findListByTopicAndLogType(topic, logType, 0, count);
            }
        }

        Result result = ResultEnum.FIND_SUCCESS.getResult();
        result.setData(mqttLogs);
        return result;
    }

    public Result deleteData(String userName, String deviceName, int dataType, String key, String value) {
        if (StrUtil.isEmpty(deviceName)) {
            return ResultEnum.DELETE_ERROR.getResult();
        }
        Device one = deviceDao.findOne(userName, deviceName);
        String upDataStreamId = one.getUpDataStreamId();

        try {
            upDataService.delete(dataType, upDataStreamId, key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEnum.DELETE_ERROR.getResult();
        }
        return ResultEnum.DELETE_SUCCESS.getResult();
    }

    public Result publish(String json, String topic) {
        try {
            mqttClient.publish(topic, 0, json);
        } catch (Exception e) {
            return ResultEnum.SEND_ERROR.getResult();
        }
        return ResultEnum.SEND_SUCCESS.getResult();
    }
}
