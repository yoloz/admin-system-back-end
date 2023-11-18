package indi.yolo.admin.system.modules.rolemenurel.service;

import com.mybatisflex.core.service.IService;
import indi.yolo.admin.system.modules.rolemenurel.entity.RoleMenuRelation;
import org.springframework.cache.annotation.CacheEvict;

import java.util.Collection;

/**
 * @author yoloz
 */
public interface IRoleMenuRelationService extends IService<RoleMenuRelation> {

    @CacheEvict(cacheNames = {"permissions", "routerMenus"}, allEntries = true)
    int addRoleMenuRelation(Integer roleId, Collection<Integer> menuIds);

    @CacheEvict(cacheNames = {"permissions", "routerMenus"}, allEntries = true)
    int delRoleMenuRelationByRole(Integer roleId);

    Collection<Integer> getMenuIdsByRole(Integer roleId);

}
