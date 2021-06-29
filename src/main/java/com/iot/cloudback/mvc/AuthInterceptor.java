package com.iot.cloudback.mvc;

import cn.hutool.core.util.ArrayUtil;
import com.iot.cloudback.aop.Auth;
import com.iot.cloudback.entity.Result;
import com.iot.cloudback.entity.Token;
import com.iot.cloudback.entity.constants.Constants;
import com.iot.cloudback.entity.constants.ResultEnum;
import com.iot.cloudback.service.TokenService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.mvc
 * @ClassName: AuthInterceptor
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 22:33
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 22:33
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 被Token自动校验注解的请求
        if (method.getAnnotation(Auth.class) != null) {
            Auth auth = method.getAnnotation(Auth.class);

            // 检查客户端传递的token值
            String tokenValue = request.getHeader(Constants.TOKEN_KEY);
            Token token = tokenService.getToken(tokenValue);
            ResultEnum resultEnum = tokenService.checkTokenBackResultEnum(token);
            Result result = resultEnum.getResult();

            // 请求头中无token值表示未登录
            // 若请求地址为用户或设备登录则放行
            if (resultEnum == ResultEnum.REQUEST_HEADER_NOT_TOKEN) {
                if (request.getRequestURI().contains(Constants.LOGIN_PASS)) {
                    return true;
                }
            }

            // token校验通过，如果请求的是登录操作直接返回登录成功
            // 若请求的不是登录操作，验证是否具备相关权限,验证成功后写入属性
            // 请求的是退出登录操作，直接删除cookie并返回
            if (resultEnum == ResultEnum.TOKEN_SUCCESS) {
                if (request.getRequestURI().contains(Constants.LOGIN_PASS)) {
                    result = ResultEnum.LOGIN_SUCCESS.getResult();
                } else if (request.getRequestURI().contains(Constants.LOGOUT_PASS)) {
                    result = ResultEnum.LOGOUT.getResult();
                } else {
                    // 检查权限
                    String type = token.getType();
                    for (String s : auth.value()) {
                        if (type.equals(s)) {
                            request.setAttribute(Constants.AUTH_KEY, type);
                            request.setAttribute(Constants.AUTH_ID, token.getId());
                            return true;
                        }
                    }
                    result = ResultEnum.NO_AUTH.getResult();
                }
            }

            // token校验错误时，删除当前Cookie中的token值
            if (result.getStatus() == 0 && (!result.getMsg().equals(ResultEnum.NO_AUTH.getResult().getMsg()))) {
                Cookie[] cookies = request.getCookies();
                if (ArrayUtil.isNotEmpty(cookies)) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(Constants.TOKEN_KEY)) {
                            cookie.setValue(null);
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                            break;
                        }
                    }
                }
            }

            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.write(JSONObject.fromObject(result).toString());
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            return false;
        }

        return true;
    }
}
