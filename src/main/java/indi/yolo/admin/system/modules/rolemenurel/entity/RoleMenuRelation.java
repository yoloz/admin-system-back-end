package indi.yolo.admin.system.modules.rolemenurel.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yoloz
 */
@Data
@Table(value = "role_menu_rel")
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuRelation {
    @Id
    private Integer roleId;
    @Id
    private Integer menuId;
}
