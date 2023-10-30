package indi.yolo.admin.system.modules.role.controller;

import com.mybatisflex.core.paginate.Page;
import indi.yolo.admin.system.commons.entity.Permission;
import indi.yolo.admin.system.commons.entity.rest.RestResult;
import indi.yolo.admin.system.modules.log.annotation.Log;
import indi.yolo.admin.system.modules.role.entity.*;
import indi.yolo.admin.system.modules.role.service.IRoleService;
import jakarta.annotation.Resource;
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
}
