package indi.yolo.admin.system.modules.role.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import indi.yolo.admin.system.modules.role.entity.Role;
import indi.yolo.admin.system.modules.role.entity.RoleDTO;
import indi.yolo.admin.system.modules.role.entity.RoleUserRelationDTO;
import indi.yolo.admin.system.modules.user.entity.User;
import org.springframework.cache.annotation.CacheEvict;

import java.util.Collection;
import java.util.List;

/**
 * @author yoloz
 */
public interface IRoleService extends IService<Role> {

    Page<Role> getRoleList(RoleDTO roleDTO);

    Page<User> getUserByRole(RoleUserRelationDTO roleUserRelationDTO);

    Integer addRole(RoleDTO roleDTO);

    boolean updateRole(RoleDTO roleDTO);

    @CacheEvict(cacheNames = "permissions,routerMenus", allEntries = true)
    Integer removeRole(Collection<Integer> roleIds);

    //[{id:"",name:""},...]查询用户可选择的角色选项
    List<Role> getRoleOptionByUser(Integer userId);
}
