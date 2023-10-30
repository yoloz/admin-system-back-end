package indi.yolo.admin.system.modules.roleuserrel.controller;

import com.mybatisflex.core.paginate.Page;
import indi.yolo.admin.system.commons.entity.Permission;
import indi.yolo.admin.system.commons.entity.rest.RestResult;
import indi.yolo.admin.system.modules.log.annotation.Log;
import indi.yolo.admin.system.modules.roleuserrel.entity.RoleUserRelationDTO;
import indi.yolo.admin.system.modules.roleuserrel.service.IRoleUserRelationService;
import indi.yolo.admin.system.modules.user.entity.User;
import indi.yolo.admin.system.modules.user.entity.UserDTO;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yolo
 */
@RestController
@RequestMapping("/roleUserRel")
public class RoleUserRelController {

    @Resource
    private IRoleUserRelationService roleUserRelationService;

    @Permission("user:role")
    @Log("编辑用户角色")
    @PostMapping("/updateByUser")
    @Transactional(rollbackFor = Exception.class)
    public RestResult<Object> updateByUser(@RequestBody UserDTO userDTO) {
        int i = roleUserRelationService.delRoleUserRelationByUser(userDTO.getId());
        if (i == 0) {
            return RestResult.error("角色更新失败!");
        }
        i = roleUserRelationService.addUserRoleRelation(userDTO.getId(), userDTO.getRoleIds());
        if (i == 0) {
            return RestResult.error("角色更新失败!");
        }
        return RestResult.success(true);
    }

    @Log("查询角色关联的用户列表")
    @PostMapping("/getUserByRole")
    public RestResult<Object> getUserByRole(@RequestBody RoleUserRelationDTO roleUserRelationDTO) {
        Page<User> users = roleUserRelationService.getUserByRole(roleUserRelationDTO);
        return RestResult.success(users);
    }

    @Permission("role:user")
    @Log("编辑角色用户")
    @PostMapping("/userRemove")
    public RestResult<Object> delRoleUserRelation(@RequestBody RoleUserRelationDTO roleUserRelationDTO) {
        int i = roleUserRelationService.delRoleUserRelation(roleUserRelationDTO.getId(), roleUserRelationDTO.getUserIds());
        if (i == 0) {
            return RestResult.error("移除用户关联失败!");
        }
        return RestResult.success(true);
    }
}
