package indi.yolo.admin.system.modules.menu.service;

import com.mybatisflex.core.service.IService;
import indi.yolo.admin.system.modules.menu.entity.Menu;
import indi.yolo.admin.system.modules.menu.entity.MenuDTO;
import indi.yolo.admin.system.modules.menu.entity.MenuVO;
import indi.yolo.admin.system.modules.menu.entity.MenuRouter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

/**
 * @author yoloz
 */
public interface IMenuService extends IService<Menu> {

    Collection<MenuVO> getMenuList(MenuDTO menuDTO);

    @Cacheable(cacheNames = "routerMenus", key = "#userId")
    Collection<MenuRouter> getRouterMenu(Integer userId);

    int addMenu(MenuDTO menuDTO);

    int updateMenu(MenuDTO menuDTO);

    @CacheEvict(cacheNames = {"permissions", "routerMenus"}, allEntries = true)
    int removeMenu(Collection<Integer> menuIds);

    //排除按钮及隐藏的菜单 [{id:"",name:"",children[...]},...]
    Collection<MenuVO> selectOptions();
}
