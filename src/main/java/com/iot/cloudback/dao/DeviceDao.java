package com.iot.cloudback.dao;

import com.iot.cloudback.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.dao
 * @ClassName: DeviceDao
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/20 12:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/20 12:16
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Repository
public class DeviceDao implements BaseDao<Device> {

    @Autowired
    private MongoTemplate template;

    @Override
    public void createCollection() {
        if (!template.collectionExists(Device.class)) {
            template.createCollection(Device.class);
        }
    }

    @Override
    public void add(Device entity) {
        template.insert(entity);
    }

    @Override
    public void update(Device entity) {
        Query query = new Query();
        query.addCriteria(new Criteria("deviceId").is(entity.getDeviceId()));
        Update update = new Update();
        updateIfNotNull(update, "userName", entity.getUserName());
        updateIfNotNull(update, "deviceId", entity.getDeviceId());
        updateIfNotNull(update, "deviceName", entity.getDeviceName());
        updateIfNotNull(update, "upDataStreamId", entity.getUpDataStreamId());
        updateIfNotNull(update, "subscribeTopic", entity.getSubscribeTopic());
        updateIfNotNull(update, "timing", entity.getTiming());
        updateIfNotNull(update, "dataType", entity.getDataType());
        updateIfNotNull(update, "isOnline", entity.getIsOnline());
        template.updateFirst(query, update, Device.class);
    }

    public void delete(int deviceId) {
        Query query = new Query();
        query.addCriteria(new Criteria("deviceId").is(deviceId));
        template.remove(query, Device.class);
    }

    public Device findOne(int deviceId) {
        Query query = new Query();
        query.addCriteria(new Criteria("deviceId").is(deviceId));
        return template.findOne(query, Device.class);
    }

    public Device findOne(String userName, String deviceName) {
        Query query = new Query();
        query.addCriteria(new Criteria("userName").is(userName));
        query.addCriteria(new Criteria("deviceName").is(deviceName));
        return template.findOne(query, Device.class);
    }

    public Device findOne(String upDataStreamId) {
        Query query = new Query(new Criteria("upDataStreamId").is(upDataStreamId));
        return template.findOne(query, Device.class);
    }

    public List<Device> findByUser(String userName) {
        Query query = new Query();
        query.addCriteria(new Criteria("userName").is(userName));
        return template.find(query, Device.class);
    }

    @Override
    public List<Device> findAll() {
        return template.findAll(Device.class);
    }
}
