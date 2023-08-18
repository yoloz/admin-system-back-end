package indi.yolo.admin.system.commons.entity.rest;


import lombok.Getter;

@Getter
public enum ResultEnum {


    SUCCESS(200, "请求成功"),
    Error(500, "内部服务器错误"),

    RE_LOGIN(201, "请重新登录"),
    NO_PERMISSION(202, "无权限操作")
    //    LOGIN_FAIL(202, "账号或密码错误"),
//    UN_LOGIN(203, "账号未登录"),
//    NO_USER(205, "账号不存在或禁用"),
//    EXPIRED_LOGIN(204, "账号已过期，请重新登录"),
    ;

    //成员变量
    private final Integer code;
    private final String message;

    ResultEnum(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
