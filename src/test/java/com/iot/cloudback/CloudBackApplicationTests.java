package com.iot.cloudback;

import com.iot.cloudback.dao.DeviceDao;
import com.iot.cloudback.dao.ForwardTableDao;
import com.iot.cloudback.dao.MqttLogDao;
import com.iot.cloudback.dao.UserDao;
import com.iot.cloudback.entity.Device;
import com.iot.cloudback.entity.ForwardTable;
import com.iot.cloudback.entity.MqttLog;
import com.iot.cloudback.entity.User;
import com.iot.cloudback.service.RedisService;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CloudBackApplicationTests {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private ForwardTableDao forwardTableDao;

    @Autowired
    private MqttLogDao mqttLogDao;

    @Test
    void contextLoads() {
        redisService.set("lyy", "111");
    }

    @Test
    void fun1() {
        User user = new User();
        user.setId(4);
        User user1 = userDao.findUser(user).get(0);
        System.out.println(user1.toString());
    }

    @Test
    void fun2() {
        String s = "{\"data\":12}";
        JSONObject jsonObject = JSONObject.fromObject(s);
        System.out.println(jsonObject.getString("data"));
    }

    @Test
    void fun3() {
        List<String> strings = new ArrayList<>();
        strings.add("/user11/ESP8266_test/get");
        ForwardTable table = new ForwardTable("/user11/Android_test/update", strings);
        forwardTableDao.add(table);
    }

    @Test
    void fun4() {
        List<String> strings = new ArrayList<>();
        strings.add("/aaa/a");
        strings.add("/bbb/b");
        ForwardTable table = new ForwardTable("/ccc/c", strings);
        table.getReceiver().add("/ddd/d");
        forwardTableDao.update(table);
    }

    @Test
    void fun5() {
        ForwardTable one = forwardTableDao.findOne("/aaa/c");
        if (one == null) {
            System.out.println("is Null!");
        } else {
            System.out.println(one.getReceiver().toString());
        }
    }

    @Test
    void fun6() {
        String topic = "^/user11";
        List<MqttLog> listByTopic = mqttLogDao.findListByTopic(topic, 0, 5);
        for (MqttLog mqttLog : listByTopic) {
            System.out.println(mqttLog);
        }
    }
}
