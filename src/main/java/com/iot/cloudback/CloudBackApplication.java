package com.iot.cloudback;

import com.iot.cloudback.entity.User;
import com.iot.cloudback.mqtt.CloudMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CloudBackApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(CloudBackApplication.class, args);
        CloudMqttClient cloudMqttClient = run.getBean(CloudMqttClient.class);
        try {
            cloudMqttClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
