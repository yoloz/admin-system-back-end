package indi.yolo.admin.system.modules.roleuserrel.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import indi.yolo.admin.system.modules.roleuserrel.entity.RoleUserRelation;
import indi.yolo.admin.system.modules.roleuserrel.entity.RoleUserRelationDTO;
import indi.yolo.admin.system.modules.user.entity.User;
import org.springframework.cache.annotation.CacheEvict;

import java.util.Collection;

/**
 * @author yoloz
 */
public interface IRoleUserRelationService extends IService<RoleUserRelation> {

    Page<User> getUserByRole(RoleUserRelationDTO roleUserRelationDTO);

    @CacheEvict(cacheNames = "permissions,routerMenus", allEntries = true)
    int addUserRoleRelation(Integer userId, Collection<Integer> roleIds);

    @CacheEvict(cacheNames = "permissions,routerMenus", allEntries = true)
    int delRoleUserRelation(Integer roleId, Collection<Integer> userIds);

    @CacheEvict(cacheNames = "permissions,routerMenus", allEntries = true)
    int delRoleUserRelationByUser(Integer userId);


}
