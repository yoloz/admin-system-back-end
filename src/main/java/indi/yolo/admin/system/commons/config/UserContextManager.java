package indi.yolo.admin.system.commons.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import indi.yolo.admin.system.commons.utils.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户登陆上下文缓存
 *
 * @author yoloz
 */
@Component
public class UserContextManager {

    @Value("${jwt.secret:yzxa2020}")
    private String secret;
    @Value("${jwt.expireTime:24}")
    private Integer expireTime;

    @Autowired
    private MapCache mapCache;

    //获取用户id
    public Integer getUserId() {
        String token = getToken();
        return Integer.parseInt(getTokenClaimByName("id", token));
    }

    //获取用户名
    public String getUserName() {
        String token = getToken();
        return getTokenClaimByName("name", token);
    }

    public boolean checkSessionExpire() {
        String sessionId = getSessionId();
        Optional<Object> optional = mapCache.get(sessionId);
        if (optional.isPresent()) {
            updateSession(sessionId);
            return true;
        } else {
            return false;
        }
    }

    private void updateSession(String sessionId) {
        mapCache.put(sessionId, sessionId, 30, TimeUnit.MINUTES);
    }

    public void removeSession() {
        mapCache.remove(getSessionId());
    }

    public String createToken(@Nonnull Integer userId, @Nonnull String username) {
        Map<String, String> playLoadMap = Map.of("id", userId + "", "name", username);
        Calendar ca = Calendar.getInstance();
        if (expireTime <= 0) {
            expireTime = 24;
        }
        ca.add(Calendar.HOUR_OF_DAY, expireTime);
        //  创建JWT的token对象
        JWTCreator.Builder builder = JWT.create();
        playLoadMap.forEach(builder::withClaim);
        // 设置发布事件
        builder.withIssuedAt(new Date());
        // 过期时间
        builder.withExpiresAt(ca.getTime());
        // 签名加密
        String token = builder.sign(Algorithm.HMAC256(secret));
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        requestAttributes.setAttribute("token", token, 0);
        updateSession(getSessionId(userId));
        return token;
    }

    private String getSessionId(Integer userId) {
        return HttpUtils.getCurrentReqIP() + "_" + userId;
    }

    private String getSessionId() {
        Integer userId = getUserId();
        return HttpUtils.getCurrentReqIP() + "_" + userId;
    }

    private String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest = ((ServletRequestAttributes) Objects.requireNonNull(requestAttributes)).getRequest();
        String token = servletRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(token) || "undefined".equals(token) || "null".equals(token)) { // login
            token = String.valueOf(requestAttributes.getAttribute("token", 0));
        }
        verifyToken(token);
        return token;
    }

    // 从token中获取到指定指定keyName的value值
    private String getTokenClaimByName(String keyName, String token) {
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaim(keyName).asString();
    }

    // 验证JwtToken 不抛出异常说明验证通过
    private void verifyToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
        jwtVerifier.verify(token);
    }
}
