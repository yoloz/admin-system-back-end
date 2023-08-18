package indi.yolo.admin.system.modules.user.entity;

import lombok.Data;

/**
 * @author yoloz
 */
@Data
public class LoginDto {

    private String username;
    private String password;

    private String uuid;
    private String code;
}
