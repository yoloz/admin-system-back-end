package indi.yolo.admin.system.modules.user.entity;

import lombok.Data;

/**
 * @author yoloz
 */
@Data
public class PwdDto {

    private String userName;
    private String oldPassword;
    private String newPassword;
}
