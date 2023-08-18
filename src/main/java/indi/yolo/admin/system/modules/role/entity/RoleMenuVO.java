package indi.yolo.admin.system.modules.role.entity;

import indi.yolo.admin.system.modules.menu.entity.MenuVO;
import lombok.Data;

import java.util.Collection;

/**
 * @author yoloz
 */
@Data
public class RoleMenuVO {

    //所有菜单列表
    Collection<MenuVO> menus;
    //角色已经配置的菜单id
    Collection<Integer> keys;

}
