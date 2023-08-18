package indi.yolo.admin.system.modules.user.entity;

import indi.yolo.admin.system.modules.role.entity.Role;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * @author yoloz
 */
@Data
public class UserVO {

    private Integer id;
    private String nickname;
    private String username;
    private boolean enable;
    private boolean builtin;
    private String phone;
    private String email;
    private String loginIp;
    private Timestamp loginTime;
    private Collection<Role> roles;
}
