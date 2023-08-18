package indi.yolo.admin.system.modules.user.entity;

import indi.yolo.admin.system.commons.entity.base.BaseDTO;
import lombok.Data;

import java.util.Collection;

/**
 * @author yoloz
 */
@Data
public class UserDTO extends BaseDTO {

    private Integer id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String phone;
    private Boolean enable;
    private Collection<Integer> roleIds;

}
