package indi.yolo.admin.system.modules.user.mapstruct;

import indi.yolo.admin.system.commons.entity.base.BaseMapStruct;
import indi.yolo.admin.system.modules.user.entity.User;
import indi.yolo.admin.system.modules.user.entity.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yoloz
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapStruct extends BaseMapStruct<UserDTO, User> {
}
