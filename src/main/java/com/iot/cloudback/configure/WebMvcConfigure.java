package com.iot.cloudback.configure;

import com.iot.cloudback.mvc.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.configure
 * @ClassName: WebMvcConfigure
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 22:39
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 22:39
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SpringBootConfiguration
public class WebMvcConfigure implements WebMvcConfigurer {

    private static final String ORIGINS[] = new String[] { "GET", "POST", "PUT", "DELETE" };

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }

    /**
     * 解决跨域问题
     *
     * @param registry 注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods(ORIGINS)
                .maxAge(3600);
    }
}
