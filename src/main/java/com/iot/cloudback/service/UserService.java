package com.iot.cloudback.service;

import cn.hutool.core.util.StrUtil;
import com.iot.cloudback.dao.UserDao;
import com.iot.cloudback.entity.Result;
import com.iot.cloudback.entity.Token;
import com.iot.cloudback.entity.User;
import com.iot.cloudback.entity.constants.AuthType;
import com.iot.cloudback.entity.constants.ResultEnum;
import com.iot.cloudback.entity.constants.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.service
 * @ClassName: UserService
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 22:59
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 22:59
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenService tokenService;

    public Result login(User user) {
        if (StrUtil.isEmpty(user.getName()) || StrUtil.isEmpty(user.getPassword())) {
            return ResultEnum.LOGIN_PARAM_EMPTY.getResult();
        }
        List<User> users = userDao.findUser(user);
        if (null == users || users.size() == 0) {
            return ResultEnum.LOGIN_ERROR.getResult();
        } else {
            String type = null;
            switch (users.get(0).getAuthority()) {
                case AuthType.USER:
                    type = TokenType.USER;
                    break;
                case AuthType.DEVELOPER:
                    type = TokenType.DEVELOPER;
                    break;
                case AuthType.ADMIN:
                    type = TokenType.ADMIN;
                    break;
                default:
                    ;
            }

            Token token = tokenService.createToken(type, users.get(0).getId());
            Result result = ResultEnum.LOGIN_SUCCESS.getResult();
            result.setData(token.getTokenValue());
            return result;
        }
    }

    public Result updateInfo(User user) {
        int affectedRowCount = userDao.updateUserInfo(user);
        if (affectedRowCount == 1) {
            User find = userDao.findUser(user).get(0);
            String type = null;
            switch (find.getAuthority()) {
                case AuthType.USER:
                    type = TokenType.USER;
                    break;
                case AuthType.DEVELOPER:
                    type = TokenType.DEVELOPER;
                    break;
                case AuthType.ADMIN:
                    type = TokenType.ADMIN;
                    break;
                default:
                    ;
            }

            Token token = tokenService.createToken(type, find.getId());
            Result result = ResultEnum.UPDATE_SUCCESS.getResult();
            result.setData(token.getTokenValue());
            return result;
        } else {
            return ResultEnum.UPDATE_ERROR.getResult();
        }
    }

    public Result addUser(User user) throws DataAccessException {
        int affectedRowCount = userDao.addUser(user);
        if (affectedRowCount == 1) {
            return ResultEnum.ADD_SUCCESS.getResult();
        } else {
            return ResultEnum.ADD_ERROR.getResult();
        }
    }

    public Result deleteUser(User user) {
        int affectedRowCount = userDao.deleteUser(user);
        if (affectedRowCount == 1) {
            return ResultEnum.DELETE_SUCCESS.getResult();
        } else {
            return ResultEnum.DELETE_ERROR.getResult();
        }
    }

    public Result updateUser(User user) {
        int affectedRowCount = userDao.updateUserInfo(user);
        if (affectedRowCount == 1) {
            return ResultEnum.UPDATE_SUCCESS.getResult();
        } else {
            return ResultEnum.UPDATE_ERROR.getResult();
        }
    }

    public Result findUser(User user) {
        Result result = ResultEnum.FIND_SUCCESS.getResult();
        List<User> users = userDao.findUser(user);
        result.setData(users.get(0));
        return result;
    }

    public Result findAllUser() {
        Result result = ResultEnum.FIND_SUCCESS.getResult();
        List<User> users = userDao.findUser(new User());
        result.setData(users);
        return result;
    }

    public Result userCenter(String auth) {
        Result result = ResultEnum.YES_AUTH.getResult();
        switch (auth) {
            case TokenType.USER:
                result.setData("/usercenter.html");
                break;
            case TokenType.ADMIN:
                result.setData("/admincenter.html");
                break;
            default:
                ;
        }
        return result;
    }

    public Result deviceManager(String auth) {
        Result result = ResultEnum.YES_AUTH.getResult();
        switch (auth) {
            case TokenType.USER:
                result.setData("/userdevicemanage.html");
                break;
            case TokenType.DEVELOPER:
                result.setData("/developdevicemanage.html");
                break;
            default:
                ;
        }
        return result;
    }

}
