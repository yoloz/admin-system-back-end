package indi.yolo.admin.system.commons.config;

import indi.yolo.admin.system.commons.intrcepter.AuthInterceptor;
import indi.yolo.admin.system.commons.intrcepter.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yoloz
 */
@Configuration
public class WebRequestConfigure implements WebMvcConfigurer {

    private AuthInterceptor jwtInterceptor;
    private PermissionInterceptor permissionInterceptor;

    @Autowired
    public void setJwtInterceptor(AuthInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Autowired
    public void setPermissionInterceptor(PermissionInterceptor permissionInterceptor) {
        this.permissionInterceptor = permissionInterceptor;
    }

    @Bean
    public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new OrderedHiddenHttpMethodFilter();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("*")
                .allowedOriginPatterns("*") //允许哪个请求来源进行跨域
                .allowedHeaders("*") //允许哪个请求头
                .allowCredentials(true) //是否允许携带cookie进行跨域
                .allowedMethods("GET", "POST", "DELETE", "PUT") //允许哪个方法进行跨域
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器可以设置order顺序，值越小，preHandle越先执行，postHandle和afterCompletion越后执行
        // order默认的值是0，如果只添加一个拦截器，可以不显示设置order的值
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").excludePathPatterns("/login", "/captchaImage", "/logout").order(1);
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**").order(2);
    }
}
