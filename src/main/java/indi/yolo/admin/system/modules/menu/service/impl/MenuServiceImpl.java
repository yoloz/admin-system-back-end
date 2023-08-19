package indi.yolo.admin.system.modules.menu.service.impl;

import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import indi.yolo.admin.system.modules.menu.entity.Menu;
import indi.yolo.admin.system.modules.menu.entity.MenuDTO;
import indi.yolo.admin.system.modules.menu.entity.MenuVO;
import indi.yolo.admin.system.modules.menu.entity.MenuRouter;
import indi.yolo.admin.system.modules.menu.mapper.MenuMapper;
import indi.yolo.admin.system.modules.menu.mapstruct.MenuMapStructDTO;
import indi.yolo.admin.system.modules.menu.mapstruct.MenuMapStructRouter;
import indi.yolo.admin.system.modules.menu.mapstruct.MenuMapStructVO;
import indi.yolo.admin.system.modules.menu.service.IMenuService;
import indi.yolo.admin.system.modules.role.service.IRoleMenuRelationService;
import indi.yolo.admin.system.modules.menu.entity.table.MenuTableDef;
import indi.yolo.admin.system.modules.role.entity.table.RoleMenuRelationTableDef;
import indi.yolo.admin.system.modules.user.entity.table.UserRoleRelationTableDef;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mybatisflex.core.query.QueryMethods.select;

/**
 * @author yoloz
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private MenuMapStructDTO menuMapStructDTO;
    @Resource
    private MenuMapStructVO menuMapStructVO;
    @Resource
    private MenuMapStructRouter menuMapStructRouter;
    @Resource
    private IRoleMenuRelationService roleMenuRelationService;


    @Override
    public Collection<MenuVO> getMenuList(MenuDTO menuDTO) {
        QueryWrapper sql = QueryWrapper.create().select(MenuTableDef.MENU.DEFAULT_COLUMNS)
                .from(MenuTableDef.MENU).where(MenuTableDef.MENU.NAME.like(menuDTO.getName()))
                .orderBy(MenuTableDef.MENU.PID.asc(), MenuTableDef.MENU.ORDER.asc());
        List<Menu> menus = menuMapper.selectListByQuery(sql);
        List<MenuVO> menuVOS = new ArrayList<>();
        for (Menu menu : menus) {
            packMenuVO(menuVOS, menu);
        }
        return menuVOS;
    }

    @Override
    public Collection<MenuRouter> getRouterMenu(Integer userId) {
        QueryWrapper sql = QueryWrapper.create()
                .select(MenuTableDef.MENU.DEFAULT_COLUMNS)
                .from(MenuTableDef.MENU)
                .where(MenuTableDef.MENU.ID.in(select(QueryMethods.distinct(RoleMenuRelationTableDef.ROLE_MENU_RELATION.MENU_ID))
                        .from(RoleMenuRelationTableDef.ROLE_MENU_RELATION).where(RoleMenuRelationTableDef.ROLE_MENU_RELATION.ROLE_ID.in(
                                QueryMethods.select(UserRoleRelationTableDef.USER_ROLE_RELATION.ROLE_ID)
                                        .from(UserRoleRelationTableDef.USER_ROLE_RELATION)
                                        .where(UserRoleRelationTableDef.USER_ROLE_RELATION.USER_ID.eq(userId))
                        ))
                )).and(MenuTableDef.MENU.TYPE.ne(2)).orderBy(MenuTableDef.MENU.PID.asc(), MenuTableDef.MENU.ORDER.asc());
        List<Menu> menus = menuMapper.selectListByQuery(sql);
        List<MenuRouter> routerMenus = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getPid() == null || menu.getPid() == 0) {
                routerMenus.add(menuMapStructRouter.toRouter(menu));
            } else {
                packRouterMenu(routerMenus, menu);
            }
        }
        return routerMenus;
    }

    @Override
    public int addMenu(MenuDTO menuDTO) {
        Menu menu = menuMapStructDTO.toEntity(menuDTO);
        return menuMapper.insertSelective(menu);
    }

    @Override
    public int updateMenu(MenuDTO menuDTO) {
        Menu menu = menuMapStructDTO.toEntity(menuDTO);
        return menuMapper.update(menu, true);
    }

    @Override
    public int removeMenu(Collection<Integer> menuIds) {
        return menuMapper.deleteBatchByIds(menuIds);
    }

    @Override
    public Collection<MenuVO> selectOptions() {
        QueryWrapper sql = QueryWrapper.create()
                .select(MenuTableDef.MENU.ID, MenuTableDef.MENU.NAME, MenuTableDef.MENU.PID)
                .from(MenuTableDef.MENU)
                .where(MenuTableDef.MENU.TYPE.ne(2))
                .and(MenuTableDef.MENU.HIDDEN.eq(false))
                .orderBy(MenuTableDef.MENU.PID.asc(), MenuTableDef.MENU.ORDER.asc());
        List<Menu> menus = menuMapper.selectListByQuery(sql);
        List<MenuVO> menuVOS = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getPid() == null || menu.getPid() == 0) {
                menuVOS.add(menuMapStructVO.toVo(menu));
            } else {
                packMenuVO(menuVOS, menu);
            }
        }
        return menuVOS;
    }

    private void packMenuVO(Collection<MenuVO> menuVOS, Menu menu) {
        for (MenuVO vo : menuVOS) {
            if (vo.getId().equals(menu.getPid())) {
                if (vo.getChildren() == null) {
                    vo.setChildren(new ArrayList<>());
                }
                vo.getChildren().add(menuMapStructVO.toVo(menu));
                return;
            } else if (vo.getChildren() != null && !vo.getChildren().isEmpty()) {
                packMenuVO(vo.getChildren(), menu);
            }
        }
    }

    private void packRouterMenu(Collection<MenuRouter> routerMenus, Menu menu) {
        for (MenuRouter router : routerMenus) {
            if (router.getId().equals(menu.getPid())) {
                if (router.getChildren() == null) {
                    router.setChildren(new ArrayList<>());
                }
                router.getChildren().add(menuMapStructRouter.toRouter(menu));
                return;
            } else if (router.getChildren() != null && !router.getChildren().isEmpty()) {
                packRouterMenu(router.getChildren(), menu);
            }
        }
    }
}
