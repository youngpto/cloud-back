package com.iot.cloudback.dao;

import com.iot.cloudback.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.dao
 * @ClassName: UserDao
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 22:02
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 22:02
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Mapper
@Repository
public interface UserDao {

    /**
     * 增加用户
     *
     * @param user 用户信息
     * @return 影响行数
     * @throws DataAccessException 索引异常抛出
     */
    int addUser(User user) throws DataAccessException;

    /**
     * 查询用户
     *
     * @param user 查询条件
     * @return 用户集合
     */
    List<User> findUser(User user);

    /**
     * 修改用户信息
     *
     * @param user 新用户信息
     * @return 影响行数
     */
    int updateUserInfo(User user);

    /**
     * 删除用户
     *
     * @param user 删除条件
     * @return 影响行数
     */
    int deleteUser(User user);
}
