package indi.yolo.admin.system.modules.roleuserrel.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yoloz
 */
@Data
@Table(value = "role_user_rel")
@NoArgsConstructor
@AllArgsConstructor
public class RoleUserRelation {

    @Id
    private Integer roleId;
    @Id
    private Integer userId;
}
