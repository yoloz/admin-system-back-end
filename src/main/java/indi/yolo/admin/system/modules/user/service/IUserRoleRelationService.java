package indi.yolo.admin.system.modules.user.service;

import com.mybatisflex.core.service.IService;
import indi.yolo.admin.system.modules.user.entity.UserRoleRelation;
import org.springframework.cache.annotation.CacheEvict;

import java.util.Collection;

/**
 * @author yoloz
 */
public interface IUserRoleRelationService extends IService<UserRoleRelation> {

    @CacheEvict(cacheNames = "permissions,routerMenus", allEntries = true)
    int addUserRoleRelation(Integer userId, Collection<Integer> roleIds);

    @CacheEvict(cacheNames = "permissions,routerMenus", allEntries = true)
    int delRoleUserRelation(Integer roleId, Collection<Integer> userIds);

    @CacheEvict(cacheNames = "permissions,routerMenus", allEntries = true)
    int delRoleUserRelationByUser(Integer userId);
}
