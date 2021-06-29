package com.iot.cloudback.service.impl;

import com.iot.cloudback.entity.Token;
import com.iot.cloudback.entity.constants.Constants;
import com.iot.cloudback.entity.constants.ResultEnum;
import com.iot.cloudback.entity.constants.TokenType;
import com.iot.cloudback.service.RedisService;
import com.iot.cloudback.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.service.impl
 * @ClassName: TokenServiceImp
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 21:40
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 21:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisService redisService;

    @Override
    public Token createToken(String type, int id) {
        String prefix = type + "_" + id;
        String uuid = UUID.randomUUID().toString().replace("_", "-");
        String tokenValue = prefix + "_" + uuid;
        if (type.equalsIgnoreCase(TokenType.USER)
                || type.equalsIgnoreCase(TokenType.DEVELOPER)
                || type.equalsIgnoreCase(TokenType.ADMIN)) {
            redisService.set(prefix, tokenValue, Constants.TOKEN_EXPIRES_TEST, TimeUnit.SECONDS);
        } else if (type.equalsIgnoreCase(TokenType.DEVICE)) {
            redisService.set(prefix, tokenValue, Constants.TOKEN_EXPIRES_DAY, TimeUnit.DAYS);
        }
        return new Token(type, id, uuid, tokenValue);
    }

    @Override
    public boolean checkToken(Token token) {
        if (token == null) {
            return false;
        }
        String key = token.getType() + "_" + token.getId();
        String tokenValue = redisService.get(key);
        if (tokenValue == null || !tokenValue.equals(token.getTokenValue())) {
            deleteToken(key);
            return false;
        }

        //表示未超过有效时间，正常操作并重置超时时间
        updateExpire(token, key);
        return true;
    }

    @Override
    public ResultEnum checkTokenBackResultEnum(Token token) {
        if (null == token) {
            return ResultEnum.REQUEST_HEADER_NOT_TOKEN;
        }
        String key = token.getType() + "_" + token.getId();
        String tokenValue = redisService.get(key);
        if (null == tokenValue) {
            deleteToken(key);
            return ResultEnum.TOKEN_TIMEOUT;
        } else if (!tokenValue.equals(token.getTokenValue())) {
            deleteToken(key);
            return ResultEnum.TOKEN_INVALID;
        }

        //表示未超过有效时间，正常操作并重置超时时间
        updateExpire(token, key);
        return ResultEnum.TOKEN_SUCCESS;
    }

    @Override
    public void deleteToken(String key) {
        redisService.delete(key);
    }

    @Override
    public Token getToken(String tokenValue) {
        if (tokenValue == null || tokenValue.length() == 0) {
            return null;
        }
        String[] param = tokenValue.split("_");
        if (param.length != Constants.TOKEN_SPLIT_LENGTH) {
            return null;
        }
        String type = param[0];
        int id = Integer.parseInt(param[1]);
        String uuid = param[2];
        return new Token(type, id, uuid, tokenValue);
    }

    @Override
    public void updateExpire(Token token, String key) {
        if (token.getType().equalsIgnoreCase(TokenType.USER)
                || token.getType().equalsIgnoreCase(TokenType.DEVELOPER)
                || token.getType().equalsIgnoreCase(TokenType.ADMIN)) {
            redisService.setExpire(key, Constants.TOKEN_EXPIRES_TEST, TimeUnit.SECONDS);
        } else if (token.getType().equalsIgnoreCase(TokenType.DEVICE)) {
            redisService.setExpire(key, Constants.TOKEN_EXPIRES_DAY, TimeUnit.DAYS);
        }
    }
}
