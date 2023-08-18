package indi.yolo.admin.system.modules.menu.mapstruct;

import indi.yolo.admin.system.commons.entity.base.BaseMapStruct;
import indi.yolo.admin.system.modules.menu.entity.Menu;
import indi.yolo.admin.system.modules.menu.entity.MenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yoloz
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapStructDTO extends BaseMapStruct<MenuDTO, Menu> {

}
