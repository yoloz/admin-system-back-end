package indi.yolo.admin.system.commons.entity.rest;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RestResult<T> implements Serializable {

    //用作唯一标识符，供序列化和反序列化时检测是否一致
    @Serial
    private static final long serialVersionUID = 7498483649536881777L;

    //标识代码
    private Integer code;

    //提示信息，通常供报错时使用
    private String msg;

    //正常返回时返回的数据
    private T data;

    public RestResult(Integer status, String message, T data) {
        this.code = status;
        this.msg = message;
        this.data = data;
    }

    static Integer successCode = ResultEnum.SUCCESS.getCode();
    static String successMsg = ResultEnum.SUCCESS.getMessage();

    static Integer errorCode = ResultEnum.Error.getCode();
    static String errorMsg = ResultEnum.Error.getMessage();

    //返回成功数据
    public static <T> RestResult<T> success(T data) {
        return new RestResult<>(successCode, successMsg, data);
    }

    public static <T> RestResult<T> error() {
        return new RestResult<>(errorCode, errorMsg, null);
    }

    public static <T> RestResult<T> error(String message) {
        return new RestResult<>(errorCode, message, null);
    }

    public static <T> RestResult<T> error(Integer code, String message) {
        return new RestResult<>(code, message, null);
    }

    public static <T> RestResult<T> error(ResultEnum rest) {
        return new RestResult<>(rest.getCode(), rest.getMessage(), null);
    }

    public static <T> RestResult<T> error(ResultEnum rest, String message) {
        return new RestResult<>(rest.getCode(), message, null);
    }
}
