package com.iot.cloudback.contoller;

import com.iot.cloudback.aop.Auth;
import com.iot.cloudback.entity.Result;
import com.iot.cloudback.entity.User;
import com.iot.cloudback.entity.constants.Constants;
import com.iot.cloudback.entity.constants.ResultEnum;
import com.iot.cloudback.entity.constants.TokenType;
import com.iot.cloudback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.contoller
 * @ClassName: UserController
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 22:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 22:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Auth({TokenType.USER, TokenType.DEVELOPER, TokenType.ADMIN})
    @PostMapping("/login")
    @ResponseBody
    public Result login(User user, HttpServletResponse response, HttpServletRequest request) {
        Result login = userService.login(user);
        Cookie cookie = new Cookie(Constants.TOKEN_KEY, (String) login.getData());
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setPath("/");
        response.addCookie(cookie);
        return login;
    }

    @Auth({TokenType.USER, TokenType.DEVELOPER, TokenType.ADMIN})
    @GetMapping("/auto_login")
    @ResponseBody
    public Result autoLogin() {
        return ResultEnum.NO_AUTO_INFO.getResult();
    }

    @Auth({TokenType.USER, TokenType.DEVELOPER, TokenType.ADMIN})
    @GetMapping("/logout")
    @ResponseBody
    public Result logout() {
        return null;
    }

    @PostMapping("/register")
    @ResponseBody
    public Result register(User user) {
        try {
            return userService.addUser(user);
        } catch (DataAccessException e) {
            final Throwable throwable = e.getCause();
            e.printStackTrace();
        }
        return ResultEnum.ADD_ERROR.getResult();
    }

    @Auth({TokenType.USER, TokenType.DEVELOPER, TokenType.ADMIN})
    @GetMapping("/check")
    @ResponseBody
    public Result check() {
        return ResultEnum.YES_AUTH.getResult();
    }

    @Auth({TokenType.USER, TokenType.ADMIN})
    @GetMapping("/user_center")
    @ResponseBody
    public Result userCenter(HttpServletRequest request) {
        String attribute = (String) request.getAttribute(Constants.AUTH_KEY);
        return userService.userCenter(attribute);
    }

    @Auth({TokenType.USER, TokenType.DEVELOPER})
    @GetMapping("/device_manager")
    @ResponseBody
    public Result deviceManager(HttpServletRequest request) {
        String attribute = (String) request.getAttribute(Constants.AUTH_KEY);
        return userService.deviceManager(attribute);
    }

    @Auth(TokenType.USER)
    @GetMapping("/find_user_info")
    @ResponseBody
    public Result findUserInfo(HttpServletRequest request) {
        int id = (int) request.getAttribute(Constants.AUTH_ID);
        User user = new User();
        user.setId(id);
        try {
            return userService.findUser(user);
        } catch (Exception e) {
            return ResultEnum.FIND_ERROR.getResult();
        }
    }

    @Auth(TokenType.USER)
    @PutMapping("/user_update")
    @ResponseBody
    public Result updateUserInfo(User user, HttpServletRequest request, HttpServletResponse response) {
        int id = (int) request.getAttribute(Constants.AUTH_ID);
        user.setId(id);
        Result result = userService.updateInfo(user);
        String data = (String) result.getData();
        Cookie cookie = new Cookie(Constants.TOKEN_KEY, data);
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setPath("/");
        response.addCookie(cookie);
        return result;
    }

    @Auth({TokenType.ADMIN,TokenType.DEVELOPER})
    @GetMapping("/find_All_User")
    @ResponseBody
    public Result findAllUser() {
        try {
            return userService.findAllUser();
        } catch (Exception e) {
            return ResultEnum.FIND_ERROR.getResult();
        }
    }

    @Auth(TokenType.ADMIN)
    @PutMapping("/user_update_info")
    @ResponseBody
    public Result updateUser(User user) {
        System.out.println(user.toString());
        return userService.updateUser(user);
    }

    @Auth(TokenType.ADMIN)
    @DeleteMapping("/delete")
    @ResponseBody
    public Result delete(User user) {
        return userService.deleteUser(user);
    }
}
