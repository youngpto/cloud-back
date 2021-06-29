package com.iot.cloudback.service;

import com.iot.cloudback.entity.Token;
import com.iot.cloudback.entity.constants.ResultEnum;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.service
 * @ClassName: TokenService
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 21:36
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 21:36
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface TokenService {
    Token createToken(String type, int id);

    boolean checkToken(Token token);

    ResultEnum checkTokenBackResultEnum(Token token);

    void deleteToken(String key);

    Token getToken(String tokenValue);

    void updateExpire(Token token,String key);
}
