package indi.yolo.admin.system.modules.user.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yoloz
 */
@Data
@Table(value = "user_role_rel")
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRelation {

    @Id
    private Integer userId;
    @Id
    private Integer roleId;
}
