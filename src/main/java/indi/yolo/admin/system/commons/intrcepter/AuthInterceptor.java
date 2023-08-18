package indi.yolo.admin.system.commons.intrcepter;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import indi.yolo.admin.system.commons.config.UserContextManager;
import indi.yolo.admin.system.commons.entity.rest.ResultEnum;
import indi.yolo.admin.system.commons.exception.UserAuthFailException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author yoloz
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private UserContextManager userContextManager;

    @Autowired
    public void setUserContextManager(UserContextManager userContextManager) {
        this.userContextManager = userContextManager;
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) throws UserAuthFailException, IOException {
        try {
            boolean active = userContextManager.checkSessionExpire();
            if (!active) {
//                throw new UserAuthFailException("帐号会话超时");
                response.sendError(ResultEnum.RE_LOGIN.getCode(), "帐号会话超时");
                return false;
            }
            return true;
        } catch (SignatureGenerationException signatureGenerationException) {
//            throw new UserAuthFailException("Token签名错误");
            response.sendError(ResultEnum.RE_LOGIN.getCode(), "Token签名错误");
            return false;
        } catch (TokenExpiredException tokenExpiredException) {
//            throw new UserAuthFailException("Token已过期");
            response.sendError(ResultEnum.RE_LOGIN.getCode(), "Token已过期");
            return false;
        } catch (AlgorithmMismatchException algorithmMismatchException) {
//            throw new UserAuthFailException("Token校验加密方法无效");
            response.sendError(ResultEnum.RE_LOGIN.getCode(), "Token校验无效");
            return false;
        }
    }
}
