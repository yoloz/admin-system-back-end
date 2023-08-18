package indi.yolo.admin.system.modules.role.mapstruct;

import indi.yolo.admin.system.commons.entity.base.BaseMapStruct;
import indi.yolo.admin.system.modules.role.entity.Role;
import indi.yolo.admin.system.modules.role.entity.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yoloz
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapStruct extends BaseMapStruct<RoleDTO, Role> {
}
