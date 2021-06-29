package com.iot.cloudback.dao;

import com.iot.cloudback.entity.Device;
import com.iot.cloudback.entity.Measurement;
import com.iot.cloudback.entity.MqttLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iot.cloudback.dao.UpDataDao.TIMING;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @ProjectName: cloud-back
 * @Package: com.iot.cloudback.dao
 * @ClassName: MqttLogDao
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/23 12:29
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/23 12:29
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Repository
public class MqttLogDao implements BaseDao<MqttLog> {
    @Autowired
    private MongoTemplate template;

    @Override
    public void createCollection() {
        if (!template.collectionExists(MqttLog.class)) {
            template.createCollection(MqttLog.class);
        }
    }

    @Override
    public void add(MqttLog entity) {
        template.insert(entity);
    }

    @Override
    public void update(MqttLog entity) {

    }

    @Override
    public List<MqttLog> findAll() {
        return template.findAll(MqttLog.class);
    }

    public List<MqttLog> findListByTopic(String topic, int skip, int limit) {
        Query query = query(where("topic").regex(topic))
                .with(Sort.by(Sort.Direction.DESC, TIMING))
                .skip(skip)
                .limit(limit);
        return template.find(query, MqttLog.class);
    }

    public List<MqttLog> findListByTopicAndLogType(String topic, int logType, int skip, int limit) {
        Query query = new Query();
        query.addCriteria(new Criteria("topic").regex(topic));
        query.addCriteria(new Criteria("logType").is(logType));
        query.with(Sort.by(Sort.Direction.DESC, TIMING));
        query.skip(skip);
        query.limit(limit);
        return template.find(query, MqttLog.class);
    }

    public List<MqttLog> findListByLogType(int logType, int skip, int limit) {
        Query query = query(where("logType").is(logType))
                .with(Sort.by(Sort.Direction.DESC, TIMING))
                .skip(skip)
                .limit(limit);
        return template.find(query, MqttLog.class);
    }

    public List<MqttLog> findList(int skip, int limit) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, TIMING));
        query.skip(skip);
        query.limit(limit);
        return template.find(query, MqttLog.class);
    }
}
