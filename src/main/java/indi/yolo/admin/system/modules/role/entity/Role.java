package indi.yolo.admin.system.modules.role.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import indi.yolo.admin.system.commons.entity.base.BasePO;
import lombok.Data;

/**
 * @author yoloz
 */
@Data
@Table(value = "role")
public class Role extends BasePO {

    @Id(keyType = KeyType.Auto)
    private Integer id;
    private String name;
    // 角色级别，数值越小，级别越大
    private Integer level;
    //内置or自定义
    @Column(onInsertValue = "false")
    private boolean builtin;
    // 描述
    private String desc;
}
