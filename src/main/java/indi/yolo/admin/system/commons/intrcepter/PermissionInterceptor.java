package indi.yolo.admin.system.commons.intrcepter;

import indi.yolo.admin.system.commons.config.UserContextManager;
import indi.yolo.admin.system.commons.entity.rest.ResultEnum;
import indi.yolo.admin.system.commons.exception.PermissionFailException;
import indi.yolo.admin.system.commons.entity.Permission;
import indi.yolo.admin.system.modules.user.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collection;

/**
 * @author yoloz
 */
@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Resource
    private UserContextManager userContextManager;

    @Resource
    private IUserService userService;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler)
            throws PermissionFailException, IOException {
        if (handler instanceof HandlerMethod handlerMethod) {
            // 获取用户权限校验注解(优先获取方法，无则再从类获取)
            Permission annotation = handlerMethod.getMethod().getAnnotation(Permission.class);
            if (null == annotation) {
                annotation = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Permission.class);
            }
            if (annotation != null) {
                Integer userId = userContextManager.getUserId();
                Collection<String> permissions = userService.getPermission(userId);
                for (String p : annotation.value()) {
                    if (permissions.contains(p)) {
                        return true;
                    }
                }
//                throw new PermissionFailException("无权限操作[" + request.getMethod() + "]");
                response.sendError(ResultEnum.NO_PERMISSION.getCode(), "无权限操作,请联系管理员");
                return false;
            }
            return true;
        }
        return true;
    }
}
