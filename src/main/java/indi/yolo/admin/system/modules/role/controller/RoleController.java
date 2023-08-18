package indi.yolo.admin.system.modules.role.controller;

import com.mybatisflex.core.paginate.Page;
import indi.yolo.admin.system.commons.entity.rest.RestResult;
import indi.yolo.admin.system.modules.log.annotation.Log;
import indi.yolo.admin.system.modules.menu.entity.MenuDTO;
import indi.yolo.admin.system.modules.menu.entity.MenuVO;
import indi.yolo.admin.system.modules.menu.service.IMenuService;
import indi.yolo.admin.system.commons.entity.Permission;
import indi.yolo.admin.system.modules.role.service.IRoleMenuRelationService;
import indi.yolo.admin.system.modules.role.service.IRoleService;
import indi.yolo.admin.system.modules.user.entity.User;
import indi.yolo.admin.system.modules.user.service.IUserRoleRelationService;
import indi.yolo.admin.system.modules.role.entity.*;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author yoloz
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;
    @Resource
    private IUserRoleRelationService userRoleRelationService;
    @Resource
    private IMenuService menuService;
    @Resource
    private IRoleMenuRelationService roleMenuRelationService;

    @Permission("role:list")
    @Log("查询角色列表")
    @PostMapping("/list")
    public RestResult<Object> getRoleList(@RequestBody RoleDTO roleDTO) {
        Page<Role> result = roleService.getRoleList(roleDTO);
        return RestResult.success(result);
    }

    @Permission("role:add")
    @Log("创建角色")
    @PostMapping("/add")
    public RestResult<Object> createRole(@RequestBody RoleDTO roleDTO) {
        Integer i = roleService.addRole(roleDTO);
        if (i == 0) {
            return RestResult.error("创建失败!");
        }
        return RestResult.success(true);
    }

    @Permission("role:edit")
    @Log("编辑角色")
    @PostMapping("/edit")
    public RestResult<Object> updateRole(@RequestBody RoleDTO roleDTO) {
        boolean bool = roleService.updateRole(roleDTO);
        if (!bool) {
            return RestResult.error("编辑失败!");
        }
        return RestResult.success(true);
    }

    @Permission("role:delete")
    @Log("删除角色")
    @PostMapping("/delete")
    public RestResult<Object> removeRole(@RequestBody Collection<Integer> roleIds) {
        Integer i = roleService.removeRole(roleIds);
        if (i == 0) {
            return RestResult.error("删除失败!");
        }
        return RestResult.success(true);
    }

    @Log("查询用户关联的角色列表")
    @PostMapping("/optionByUser")
    public RestResult<Object> getRoleOptionByUser(@RequestBody Integer userId) {
        return RestResult.success(roleService.getRoleOptionByUser(userId));
    }

    @Log("查询角色关联的用户列表")
    @PostMapping("/userList")
    public RestResult<Object> getUserByRole(@RequestBody RoleUserRelationDTO roleUserRelationDTO) {
        Page<User> users = roleService.getUserByRole(roleUserRelationDTO);
        return RestResult.success(users);
    }

    @Permission("role:user")
    @Log("编辑角色用户")
    @PostMapping("/userRemove")
    public RestResult<Object> delRoleUserRelation(@RequestBody RoleUserRelationDTO roleUserRelationDTO) {
        int i = userRoleRelationService.delRoleUserRelation(roleUserRelationDTO.getId(), roleUserRelationDTO.getUserIds());
        if (i == 0) {
            return RestResult.error("移除用户关联失败!");
        }
        return RestResult.success(true);
    }

    @Log("查询角色关联的菜单列表")
    @PostMapping("/menuList")
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
    @PostMapping("/menuEdit")
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
