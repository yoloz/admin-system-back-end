package indi.yolo.admin.system.commons.intrcepter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * @author yoloz
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    // 统一处理controller中的异常
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleControllerException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        if (request instanceof ServletWebRequest servletWebRequest) {
            return new ResponseEntity<>("请求" + servletWebRequest.getRequest().getRequestURI() + "失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("请求" + request.getDescription(true) + "失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
