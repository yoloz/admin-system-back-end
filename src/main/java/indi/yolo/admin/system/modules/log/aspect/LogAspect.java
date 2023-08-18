package indi.yolo.admin.system.modules.log.aspect;

import indi.yolo.admin.system.commons.config.UserContextManager;
import indi.yolo.admin.system.commons.entity.rest.RestResult;
import indi.yolo.admin.system.commons.entity.rest.ResultEnum;
import indi.yolo.admin.system.commons.utils.HttpUtils;
import indi.yolo.admin.system.commons.utils.JsonUtil;
import indi.yolo.admin.system.commons.utils.Utils;
import indi.yolo.admin.system.modules.log.annotation.Log;
import indi.yolo.admin.system.modules.log.entity.SysLog;
import indi.yolo.admin.system.modules.log.service.ILogService;
import indi.yolo.admin.system.modules.user.entity.LoginDto;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Resource
    private UserContextManager userContextManager;
    @Resource
    private ILogService logService;
    @Autowired
    private Ip2regionSearcher regionSearcher;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    //配置切入点
    @Pointcut("@annotation(indi.yolo.admin.system.modules.log.annotation.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    //配置环绕通知,使用在方法logPointcut()上注册的切入点
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        if (result instanceof RestResult<?> restResult) {
            SysLog syslog;
            if (Objects.equals(restResult.getCode(), ResultEnum.SUCCESS.getCode())) {
                syslog = wrapSysLog("right", joinPoint, null);
            } else {
                syslog = wrapSysLog("error", joinPoint, restResult.getMsg());
            }
            logService.save(syslog);
        } else {
            log.warn("rest result:" + result.getClass() + " syslog need to support...");
        }
        return result;
    }

    //配置异常通知
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        SysLog sysLog = wrapSysLog("error", joinPoint, e);
        logService.save(sysLog);
    }

    private SysLog wrapSysLog(String logType, JoinPoint joinPoint, Object e) {
        SysLog sysLog = new SysLog(logType, System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        if (e != null) {
            if (e instanceof Throwable throwable) {
                sysLog.setException(Utils.getStackTrace(throwable));
            } else {
                sysLog.setException(String.valueOf(e));
            }
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()"; // 方法路径
        sysLog.setAnnotation(aopLog.value()); // 描述
        String currentIP = HttpUtils.getCurrentReqIP();
        sysLog.setRequestIp(currentIP);
        IpInfo ipInfo = regionSearcher.memorySearch(currentIP);
        sysLog.setAddress(ipInfo != null ? ipInfo.getAddress() : null);
        sysLog.setMethod(methodName);
        List<Object> argList = HttpUtils.getParameter(method, joinPoint.getArgs());
        String username;
        if ("userLogin".equals(signature.getName()) && "error".equals(sysLog.getLogType())) {
            LoginDto loginDto = (LoginDto) argList.get(0);
            username = loginDto.getUsername();
        } else {
            username = userContextManager.getUserName();
        }
        sysLog.setParams(JsonUtil.obj2String(argList));
        sysLog.setUsername(username);
        sysLog.setBrowser(HttpUtils.getCurrentBrowser());
        return sysLog;
    }
}
