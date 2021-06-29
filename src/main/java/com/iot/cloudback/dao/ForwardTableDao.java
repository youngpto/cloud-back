package com.iot.cloudback.dao;

import com.iot.cloudback.entity.ForwardTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: cloud-back
 * @Package: com.iot.cloudback.dao
 * @ClassName: ForwardTableDao
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/23 0:26
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/23 0:26
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Repository
public class ForwardTableDao implements BaseDao<ForwardTable> {

    @Autowired
    private MongoTemplate template;

    @Override
    public void createCollection() {
        if (!template.collectionExists(ForwardTable.class)) {
            template.createCollection(ForwardTable.class);
        }
    }

    @Override
    public void add(ForwardTable entity) {
        template.insert(entity);
    }

    @Override
    public void update(ForwardTable entity) {
        Query query = new Query();
        query.addCriteria(new Criteria("sender").is(entity.getSender()));
        Update update = new Update();
        updateIfNotNull(update, "sender", entity.getSender());
        updateIfNotNull(update, "receiver", entity.getReceiver());
        template.updateFirst(query, update, ForwardTable.class);
    }

    public ForwardTable findOne(String sender){
        Query query = new Query();
        query.addCriteria(new Criteria("sender").is(sender));
        return template.findOne(query, ForwardTable.class);
    }

    @Override
    public List<ForwardTable> findAll() {
        return template.findAll(ForwardTable.class);
    }
}
