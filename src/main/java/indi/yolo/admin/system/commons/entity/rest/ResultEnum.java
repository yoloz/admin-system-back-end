package indi.yolo.admin.system.commons.entity.rest;

import lombok.Getter;

@Getter
public enum ResultEnum {


    SUCCESS(200, "请求成功"),
    Error(500, "内部服务器错误"),

    RE_LOGIN(201, "请重新登录"),
    NO_PERMISSION(202, "无权限操作");

    //成员变量
    private final Integer code;
    private final String message;

    ResultEnum(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
