package com.iot.cloudback.contoller;

import com.alibaba.fastjson.JSONObject;
import com.iot.cloudback.aop.Auth;
import com.iot.cloudback.entity.Device;
import com.iot.cloudback.entity.Result;
import com.iot.cloudback.entity.constants.Constants;
import com.iot.cloudback.entity.constants.ResultEnum;
import com.iot.cloudback.entity.constants.TokenType;
import com.iot.cloudback.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.contoller
 * @ClassName: DeviceController
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/20 2:05
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/20 2:05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Auth(TokenType.USER)
    @GetMapping("/find_all_device_user")
    @ResponseBody
    public Result findAllDeviceByUser(HttpServletRequest request) {
        int id = (int) request.getAttribute(Constants.AUTH_ID);
        return deviceService.findAllDeviceById(id);
    }

    @Auth(TokenType.USER)
    @PostMapping("/add_device")
    @ResponseBody
    public Result addDevice(Device device, HttpServletRequest request) {
        int id = (int) request.getAttribute(Constants.AUTH_ID);
        return deviceService.addDevice(device, id);
    }

    @Auth(TokenType.USER)
    @PutMapping("/device_update_info")
    @ResponseBody
    public Result updateDeviceInfo(Device device) {
        return deviceService.updateDeviceInfo(device);
    }

    @Auth(TokenType.USER)
    @DeleteMapping("/delete")
    @ResponseBody
    public Result delete(int deviceId) {
        return deviceService.delete(deviceId);
    }

    @Auth(TokenType.DEVELOPER)
    @GetMapping("/find_device_by_username")
    @ResponseBody
    public Result findDeviceByUsername(String userName) {
        return deviceService.findDeviceByUsername(userName);
    }

    @Auth(TokenType.USER)
    @PostMapping("/add_forward_rule")
    @ResponseBody
    public Result addForwardRule(String sender, String receiver, HttpServletRequest request) {
        int id = (int) request.getAttribute(Constants.AUTH_ID);
        return deviceService.addForwardRule(sender, receiver, id);
    }

    @Auth(TokenType.USER)
    @GetMapping("/find_data_by_type")
    @ResponseBody
    public Result findDataByDataType(String deviceName, int dataType, HttpServletRequest request) {
        int id = (int) request.getAttribute(Constants.AUTH_ID);
        return deviceService.findDataByDataType(deviceName, dataType, id);
    }

    @Auth(TokenType.DEVELOPER)
    @PostMapping("/send_message_with_device")
    @ResponseBody
    public Result sendMessageWithDevice(String userName, String deviceName, String message) {
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(message);
        } catch (Exception e) {
            return ResultEnum.ADD_ERROR.getResult();
        }
        return deviceService.sendMessageWithDevice(userName, deviceName, jsonObject);
    }

    @Auth(TokenType.DEVELOPER)
    @GetMapping("/find_mqtt_log")
    @ResponseBody
    public Result findMqttLog(int logType, int count, String userName, String deviceName) {
        return deviceService.findMqttLog(logType, count, userName, deviceName);
    }

    @Auth(TokenType.DEVELOPER)
    @DeleteMapping("/delete_data")
    @ResponseBody
    public Result deleteData(String userName, String deviceName, int dataType, String key, String value) {
        return deviceService.deleteData(userName, deviceName, dataType, key, value);
    }

    @Auth(TokenType.DEVELOPER)
    @PostMapping("/publish_cmd_by_topic")
    @ResponseBody
    public Result publishCmdByTopic(String jsonCmd, String publishTopic) {
        return deviceService.publish(jsonCmd, publishTopic);
    }
}
