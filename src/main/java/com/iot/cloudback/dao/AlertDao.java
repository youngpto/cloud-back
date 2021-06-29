package com.iot.cloudback.dao;

import cn.hutool.core.util.StrUtil;
import com.iot.cloudback.entity.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @ProjectName: cloud-back
 * @Package: com.iot.cloudback.dao
 * @ClassName: AlertDao
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/23 15:28
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/23 15:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Repository
public class AlertDao implements UpDataDao<Alert> {
    @Autowired
    private MongoTemplate template;

    @Override
    public void createCollection() {
        if (!template.collectionExists(Alert.class)) {
            template.createCollection(Alert.class);
        }
    }

    @Override
    public void add(Alert entity) {
        template.insert(entity);
    }

    @Override
    public void update(Alert entity) {

    }

    @Override
    public List<Alert> findAll() {
        return null;
    }

    @Override
    public Alert findLatestOneByUdsId(String upDateStreamId) {
        Query query = query(where(UDS_ID).is(upDateStreamId))
                .with(Sort.by(Sort.Direction.DESC, TIMING));
        return template.findOne(query, Alert.class);
    }

    @Override
    public List<Alert> findListByUdsId(String upDateStreamId, int skip, int limit) {
        Query query = query(where(UDS_ID).is(upDateStreamId))
                .with(Sort.by(Sort.Direction.DESC, TIMING))
                .skip(skip)
                .limit(limit);
        return template.find(query, Alert.class);
    }

    @Override
    public List<Alert> findListByUdsIdWithPeriod(String upDateStreamId, Date begin, Date end) {
        Query query = query(where(UDS_ID).is(upDateStreamId).and(TIMING).gte(begin).lte(end))
                .with(Sort.by(Sort.Direction.DESC, TIMING));
        return template.find(query, Alert.class);
    }

    @Override
    public void delete(String upDateStreamId, String key, String value) {
        template.remove(deleteQuery(upDateStreamId, key, value), Alert.class);
    }

    @Override
    public List<Alert> findAll(String upDateStreamId) {
        Query query = new Query();
        query.addCriteria(new Criteria(UDS_ID).is(upDateStreamId));
        return template.find(query, Alert.class);
    }
}
