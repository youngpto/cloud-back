package com.iot.cloudback.dao;

import com.iot.cloudback.entity.Alert;
import com.iot.cloudback.entity.Waypoint;
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
 * @ClassName: WaypointDao
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/23 15:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/23 15:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Repository
public class WaypointDao implements UpDataDao<Waypoint> {
    @Autowired
    private MongoTemplate template;

    @Override
    public void createCollection() {
        if (!template.collectionExists(Waypoint.class)) {
            template.createCollection(Waypoint.class);
        }
    }

    @Override
    public void add(Waypoint entity) {
        template.insert(entity);
    }

    @Override
    public void update(Waypoint entity) {

    }

    @Override
    public List<Waypoint> findAll() {
        return null;
    }

    @Override
    public Waypoint findLatestOneByUdsId(String upDateStreamId) {
        Query query = query(where(UDS_ID).is(upDateStreamId))
                .with(Sort.by(Sort.Direction.DESC, TIMING));
        return template.findOne(query, Waypoint.class);
    }

    @Override
    public List<Waypoint> findListByUdsId(String upDateStreamId, int skip, int limit) {
        Query query = query(where(UDS_ID).is(upDateStreamId))
                .with(Sort.by(Sort.Direction.DESC, TIMING))
                .skip(skip)
                .limit(limit);
        return template.find(query, Waypoint.class);
    }

    @Override
    public List<Waypoint> findListByUdsIdWithPeriod(String upDateStreamId, Date begin, Date end) {
        Query query = query(where(UDS_ID).is(upDateStreamId).and(TIMING).gte(begin).lte(end))
                .with(Sort.by(Sort.Direction.DESC, TIMING));
        return template.find(query, Waypoint.class);
    }

    @Override
    public void delete(String upDateStreamId, String key, String value) {
        template.remove(deleteQuery(upDateStreamId, key, value), Waypoint.class);
    }

    @Override
    public List<Waypoint> findAll(String upDateStreamId) {
        Query query = new Query();
        query.addCriteria(new Criteria(UDS_ID).is(upDateStreamId));
        return template.find(query, Waypoint.class);
    }
}
