package indi.yolo.admin.system.modules.rolemenurel.controller;

import indi.yolo.admin.system.commons.entity.Permission;
import indi.yolo.admin.system.commons.entity.rest.RestResult;
import indi.yolo.admin.system.modules.log.annotation.Log;
import indi.yolo.admin.system.modules.menu.entity.MenuDTO;
import indi.yolo.admin.system.modules.menu.entity.MenuVO;
import indi.yolo.admin.system.modules.menu.service.IMenuService;
import indi.yolo.admin.system.modules.role.entity.RoleMenuVO;
import indi.yolo.admin.system.modules.rolemenurel.entity.RoleMenuRelationDTO;
import indi.yolo.admin.system.modules.rolemenurel.service.IRoleMenuRelationService;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author yolo
 */
@RestController
@RequestMapping("/roleMenuRel")
public class RoleMenuRelController {

    @Resource
    private IMenuService menuService;
    @Resource
    private IRoleMenuRelationService roleMenuRelationService;

    @Log("查询角色关联的菜单列表")
    @PostMapping("/getMenuByRole")
    public RestResult<Object> getMenuByRole(@RequestBody RoleMenuRelationDTO roleMenuRelationDTO) {
        MenuDTO menuDTO = new MenuDTO();
        Collection<MenuVO> menuVOS = menuService.getMenuList(menuDTO);
        Collection<Integer> checked = roleMenuRelationService.getMenuIdsByRole(roleMenuRelationDTO.getId());
        RoleMenuVO roleMenuVO = new RoleMenuVO();
        roleMenuVO.setMenus(menuVOS);
        roleMenuVO.setKeys(checked);
        return RestResult.success(roleMenuVO);
    }

    @Permission("role:menu")
    @Log("编辑角色菜单")
    @PostMapping("/updateRoleMenu")
    @Transactional(rollbackFor = Exception.class)
    public RestResult<Object> updateRoleMenu(@RequestBody RoleMenuRelationDTO roleMenuRelationDTO) {
        int i;
        i = roleMenuRelationService.delRoleMenuRelationByRole(roleMenuRelationDTO.getId());
        i = roleMenuRelationService.addRoleMenuRelation(roleMenuRelationDTO.getId(), roleMenuRelationDTO.getMenuIds());
        if (i == 0) {
            return RestResult.error("菜单更新失败!");
        }
        return RestResult.success(true);
    }
}
