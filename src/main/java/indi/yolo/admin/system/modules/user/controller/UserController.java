package indi.yolo.admin.system.modules.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.row.Row;
import indi.yolo.admin.system.commons.config.UserContextManager;
import indi.yolo.admin.system.commons.entity.Permission;
import indi.yolo.admin.system.commons.entity.rest.RestResult;
import indi.yolo.admin.system.commons.entity.rest.ResultEnum;
import indi.yolo.admin.system.modules.log.annotation.Log;
import indi.yolo.admin.system.modules.role.service.IRoleService;
import indi.yolo.admin.system.modules.roleuserrel.service.IRoleUserRelationService;
import indi.yolo.admin.system.modules.user.entity.PwdDto;
import indi.yolo.admin.system.modules.user.entity.UserDTO;
import indi.yolo.admin.system.modules.user.entity.UserVO;
import indi.yolo.admin.system.modules.user.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author yoloz
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;
    @Resource
    private IRoleUserRelationService roleUserRelationService;
    @Resource
    private IRoleService roleService;
    @Resource
    private UserContextManager userContextManager;

    @Permission("user:list")
    @Log("查询用户列表")
    @PostMapping("/list")
    public RestResult<Object> getUserList(@RequestBody UserDTO userDto) {
        Page<UserVO> result = userService.getUserList(userDto);
        return RestResult.success(result);
    }

    @GetMapping("/userInfo")
    public RestResult<Object> getUserInfo() {
        Integer userId = userContextManager.getUserId();
        Optional<Row> optional = userService.getUserInfo(userId);
        if (optional.isEmpty()) {
            return RestResult.error(ResultEnum.RE_LOGIN, "登陆用户不存在,请重新登陆!");
        }
        Row row = optional.get();
        String token = userContextManager.createToken(row.getInt("id"), row.getString("username"));
        row.put("token", token);
        Map<String, Object> result = new HashMap<>(2);
        result.put("user", row);
        Collection<String> permissions = userService.getPermission(userId);
        result.put("permission", permissions);
        return RestResult.success(result);
    }

    @Permission("user:add")
    @Log("创建用户")
    @PostMapping("/add")
    public RestResult<Object> createUser(@RequestBody UserDTO userDto) {
        Integer userId = userService.addUser(userDto);
        int i = roleUserRelationService.addUserRoleRelation(userId, userDto.getRoleIds());
        if (i == 0) {
            return RestResult.error("用户角色关联失败!");
        }
        return RestResult.success(true);
    }

    @Permission("user:edit")
    @Log("编辑用户")
    @PostMapping("/edit")
    public RestResult<Object> updateUser(@RequestBody UserDTO userDto) {
        boolean bool = userService.updateUser(userDto);
        if (!bool) {
            return RestResult.error("编辑失败!");
        }
        return RestResult.success(true);
    }

    @Permission("user:delete")
    @Log("删除用户")
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public RestResult<Object> removeUser(@RequestBody Collection<Integer> ids) {
        Integer i = userService.removeUser(ids);
        if (i == 0) {
            return RestResult.error("删除失败!");
        }
        return RestResult.success(true);
    }

    @Permission("user:pwd")
    @Log("重置密码")
    @PostMapping("/resetPwd")
    public RestResult<Object> resetPwd(@RequestBody UserDTO userDTO) {
        boolean bool = userService.updatePwd(userDTO.getId(), userDTO.getPassword());
        if (!bool) {
            return RestResult.error("重置密码失败!");
        }
        return RestResult.success(true);
    }

    @Log("更新密码")
    @PostMapping("/updatePwd")
    public RestResult<Object> updatePwd(@RequestBody PwdDto pwdDto) {
        Integer userId = userService.getIdByNameAndPwd(pwdDto.getUserName(), pwdDto.getOldPassword());
        if (userId == null) {
            return RestResult.error("当前密码错误!");
        }
        boolean bool = userService.updatePwd(userId, pwdDto.getNewPassword());
        if (!bool) {
            return RestResult.error("更新密码失败!");
        }
        return RestResult.success(true);
    }

    @Permission("user:enable")
    @Log("用户启(停)用")
    @PostMapping("/changeEnable")
    public RestResult<Object> changeEnable(@RequestBody UserDTO userDTO) {
        boolean bool = userService.changeEnable(userDTO.getId(), userDTO.getEnable());
        if (!bool) {
            return RestResult.error("操作失败!");
        }
        return RestResult.success(true);
    }

}
