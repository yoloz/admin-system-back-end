package indi.yolo.admin.system.modules.role.entity;

import indi.yolo.admin.system.commons.entity.base.BaseDTO;
import lombok.Data;

import java.util.Collection;

/**
 * @author yoloz
 */
@Data
public class RoleMenuRelationDTO extends BaseDTO {

    private Integer id;
    private Collection<Integer> menuIds;
}
