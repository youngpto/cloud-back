package com.iot.cloudback.mqtt;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.cloudback.dao.DeviceDao;
import com.iot.cloudback.dao.ForwardTableDao;
import com.iot.cloudback.dao.MqttLogDao;
import com.iot.cloudback.entity.Device;
import com.iot.cloudback.entity.ForwardTable;
import com.iot.cloudback.entity.MqttLog;
import com.iot.cloudback.service.UpDataService;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: cloud-back
 * @Package: com.iot.cloudback.mqtt
 * @ClassName: CloudMqttClient
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/22 18:22
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/22 18:22
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Component
public class CloudMqttClient {

    @Autowired
    private ForwardTableDao forwardTableDao;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private MqttLogDao mqttLogDao;

    @Autowired
    private UpDataService upDataService;

    public static final String HOST = "tcp://47.96.125.238:1883";

    public static final String CLIENT_ID = "iot_cloud_client";

    private MqttClient mqttClient;

    public void connect() throws MqttException {
        mqttClient = new MqttClient(HOST, CLIENT_ID, new MemoryPersistence());

        MqttConnectOptions options = new MqttConnectOptions();

        options.setCleanSession(true);

        options.setConnectionTimeout(100);

        options.setKeepAliveInterval(180);

        options.setAutomaticReconnect(true);

        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                System.out.println(new Date() + "连接成功");
                if (reconnect) {
                    initSubscribe();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("连接断开");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println(topic + "上报数据");

                String msg = new String(message.getPayload());
                JSONObject jsonObject = JSON.parseObject(msg);
                String clientId = String.valueOf(jsonObject.get("clientid"));

                if (StrUtil.isNotEmpty(clientId)) {
                    if (topic.endsWith("disconnected")) {
                        System.out.println("客户端已掉线：" + clientId);
                        String[] clientAuth = clientId.split("_");
                        if (clientAuth.length == 2) {
                            setDeviceOnline(clientAuth[0], clientAuth[1], 0);
                        }
                        return;
                    } else if (topic.endsWith("connected")) {
                        System.out.println("客户端已上线：" + clientId);
                        String[] clientAuth = clientId.split("_");
                        if (clientAuth.length == 2) {
                            setDeviceOnline(clientAuth[0], clientAuth[1], 1);
                        }
                        return;
                    }
                }

                /*
                 * 订阅回调执行
                 * <pre>
                 *      1、将json格式数据存入日志表
                 *      2、根据数据规则解析数据中是否含有规定的data标签插入对应数据表
                 *      3、检索数据转发表，向目标topic发送数据
                 *      4、修改对应设备的数据上报时间
                 * </pre>
                 */
                // 1
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("1");
                        addLog(1, topic, jsonObject);
                    }
                }).start();

                // 2
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addUpData(topic, jsonObject);
                    }
                }).start();

                // 3
                ForwardTable one = forwardTableDao.findOne(topic);
                if (null != one) {
                    for (String receiver : one.getReceiver()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    addLog(2, receiver, jsonObject);
                                    mqttClient.publish(receiver, message);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }

                // 4
                String[] split = topic.split("/");
                if (split.length == 4 && split[0].equals("") && split[3].equals("update")) {
                    Device update = deviceDao.findOne(topic);
                    update.setTiming(new Date());
                    deviceDao.update(update);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("平台发布完成");
            }
        });

        mqttClient.connect(options);

        initSubscribe();
    }

    public void publish(String topic, int qos, String msg) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(false);
        mqttMessage.setPayload(msg.getBytes());
        try {
            mqttClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            mqttClient.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String[] topics) {
        try {
            mqttClient.subscribe(topics);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void initSubscribe() {
        List<Device> all = deviceDao.findAll();
        String[] topics = new String[all.size() + 2];
        for (int i = 0; i < all.size(); i++) {
            topics[i + 2] = all.get(i).getUpDataStreamId();
        }
        topics[0] = "$SYS/brokers/+/clients/#";
        topics[1] = "cloud/view/update";
        subscribe(topics);
//        subscribe("$SYS/brokers/+/clients/#");//上下线通知
//        subscribe("cloud/view/update");
    }

    public void addLog(int logType, String topic, MqttMessage mqttMessage) {
        JSONObject jsonObject = JSONObject.parseObject(new String(mqttMessage.getPayload()));
        addLog(logType, topic, jsonObject);
    }

    public void addLog(int logType, String topic, JSONObject jsonObject) {
        MqttLog mqttLog = new MqttLog();
        mqttLog.setLogType(logType);
        mqttLog.setTiming(new Date());
        mqttLog.setJsonObject(jsonObject);
        mqttLog.setTopic(topic);
        mqttLogDao.add(mqttLog);
    }

    public void addUpData(String topic, JSONObject jsonObject) {
        if (jsonObject.containsKey("data")) {
            JSONObject data = jsonObject.getJSONObject("data");
            int dataType = data.getIntValue("dataType");
            String value = data.getString("value");
            upDataService.uploadData(topic, dataType, value);
        }
    }

    public void unSubscribe(String topic) {
        try {
            mqttClient.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setDeviceOnline(String userName, String deviceName, int isOnline) {
        Device one = deviceDao.findOne(userName, deviceName);
        one.setIsOnline(isOnline);
        deviceDao.update(one);
    }
}
