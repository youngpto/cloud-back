package com.iot.cloudback.entity.constants;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.entity.constants
 * @ClassName: Constants
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 21:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 21:34
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Constants {
    /**
     * 测试使用60分钟，单位秒
     */
    public static final long TOKEN_EXPIRES_TEST = 60 * 60;

    /**
     * 用户3小时过期
     */
    public static final long TOKEN_EXPIRES_HOUR = 3;

    /**
     * 设备15天过期
     */
    public static final long TOKEN_EXPIRES_DAY = 15;

    /**
     * 登录请求不拦截
     */
    public static final String LOGIN_PASS = "login";

    /**
     * 退出登录时直接删除相关cookie
     */
    public static final String LOGOUT_PASS = "logout";

    /**
     * token分割长度
     */
    public static final int TOKEN_SPLIT_LENGTH = 3;

    /**
     * Cookie中token值的键名
     */
    public static final String TOKEN_KEY = "token_value";

    /**
     * 标记当前类型
     */
    public static final String AUTH_KEY = "auth";

    /**
     * 标记用户id
     */
    public static final String AUTH_ID = "id";

    public static final String MQTT_SERVER_HOST = "tcp://47.96.125.238:1883";

//    public static final String MQTT_SERVER_HOST = "tcp://47.96.125.238:1883";

    public static final String MQTT_CLIENT_ID = "cloud_Client";

    public static final String MQTT_USERNAME = "";

    public static final String MQTT_PASSWORD = "";
}
