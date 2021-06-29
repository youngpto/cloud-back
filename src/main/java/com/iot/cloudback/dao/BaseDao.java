package com.iot.cloudback.dao;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.dao
 * @ClassName: BaseDao
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/20 12:09
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/20 12:09
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface BaseDao<T> {

    void createCollection();

    void add(T entity);

    void update(T entity);

    List<T> findAll();

    default void updateIfNotNull(Update update, String key, String value) {
        if (StrUtil.isNotEmpty(value)) {
            update.set(key, value);
        }
    }

    default void updateIfNotNull(Update update, String key, int value) {
        if (value != -1) {
            update.set(key, value);
        }
    }

    default void updateIfNotNull(Update update, String key, Object value) {
        if (ObjectUtil.isNotEmpty(value)) {
            update.set(key, value);
        }
    }
}
