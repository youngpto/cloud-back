package com.iot.cloudback.dao;

import cn.hutool.core.util.StrUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: cloud-back
 * @Package: com.iot.cloudback.dao
 * @ClassName: UpDataDao
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/23 15:24
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/23 15:24
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface UpDataDao<T> extends BaseDao<T> {

    String UDS_ID = "upDataStreamId";

    String TIMING = "timing";

    /**
     * 根据向上通道id查询最新一条数据
     *
     * @param upDateStreamId 向上通道id
     * @return 最新一条数据
     */
    T findLatestOneByUdsId(String upDateStreamId);

    /**
     * 根据向上通道id查询指定位置指定条数的数据
     *
     * @param upDateStreamId 向上通道id
     * @param skip           指定位置
     * @param limit          指定条数
     * @return 数据集合
     */
    List<T> findListByUdsId(String upDateStreamId, int skip, int limit);

    /**
     * 根据向上通道id查询指定起止时间点的数据
     *
     * @param upDateStreamId 向上通道id
     * @param begin          起始时间
     * @param end            终止时间
     * @return 数据集合
     */
    List<T> findListByUdsIdWithPeriod(String upDateStreamId, Date begin, Date end);

    /**
     * 删除文档
     *
     * @param key   键名
     * @param value 键值
     */
    void delete(String upDateStreamId, String key, String value);

    /**
     * 根据向上通道id查询所有数据
     *
     * @param upDateStreamId topic
     * @return 数据集合
     */
    List<T> findAll(String upDateStreamId);

    default Query deleteQuery(String upDateStreamId, String key, String value){
        Criteria criteria = new Criteria();
        if (StrUtil.isNotEmpty(key)) {
            criteria = new Criteria(key).is(value);
        }
        criteria.andOperator(new Criteria(UDS_ID).is(upDateStreamId));
        Query query = new Query();
        query.addCriteria(criteria);
        return query;
    }
}
