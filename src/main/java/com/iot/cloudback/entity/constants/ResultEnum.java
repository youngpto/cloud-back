package com.iot.cloudback.entity.constants;

import com.iot.cloudback.entity.Result;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.entity.constants
 * @ClassName: ResultEnum
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 21:37
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 21:37
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public enum ResultEnum {
    /**
     * 枚举所有返回状态
     */
    LOGIN_SUCCESS(1, "登录成功", null),
    LOGIN_ERROR(0, "登录失败", null),
    REQUEST_HEADER_NOT_TOKEN(0, "请求头中无Token", null),
    TOKEN_TIMEOUT(0, "Token已过期", null),
    TOKEN_INVALID(0, "Token校验错误", null),
    TOKEN_SUCCESS(1, "Token校验通过", null),
    LOGIN_PARAM_EMPTY(0, "登录参数不得为空", null),
    NO_AUTH(0, "无操作权限", null),
    YES_AUTH(1, "操作成功", null),
    NO_AUTO_INFO(0, "无自动登录信息", null),
    LOGOUT(0, "退出登录", null),
    FIND_SUCCESS(1, "查询成功", null),
    FIND_ERROR(0, "查询失败", null),
    UPDATE_SUCCESS(1, "修改成功", null),
    UPDATE_ERROR(0, "修改失败", null),
    DELETE_SUCCESS(1, "删除成功", null),
    DELETE_ERROR(0, "删除失败", null),
    ADD_SUCCESS(1, "添加成功", null),
    ADD_ERROR(0, "添加失败", null),
    EMAIL_SUCCESS(1, "邮箱验证成功", null),
    EMAIL_ERROR(0, "邮箱验证失败", null),
    SEND_SUCCESS(1, "发布成功", null),
    SEND_ERROR(0, "发布失败", null);

    private final Result result;

    ResultEnum(int status, String msg, Object data) {
        this.result = new Result(status, msg, data);
    }

    public Result getResult() {
        return result;
    }
}
