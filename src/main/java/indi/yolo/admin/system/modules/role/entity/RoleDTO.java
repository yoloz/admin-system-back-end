package indi.yolo.admin.system.modules.role.entity;

import indi.yolo.admin.system.commons.entity.base.BaseDTO;
import lombok.Data;

/**
 * @author yoloz
 */
@Data
public class RoleDTO extends BaseDTO {

    private Integer id;
    private String name;
    private Integer level;
    private String desc;
}
