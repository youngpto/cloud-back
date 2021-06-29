package com.iot.cloudback.aop;

import com.iot.cloudback.entity.constants.TokenType;

import java.lang.annotation.*;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.aop
 * @ClassName: Auth
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 22:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 22:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Auth {
    String[] value() default {
            TokenType.USER,
            TokenType.DEVELOPER,
            TokenType.ADMIN,
            TokenType.DEVICE};
}
