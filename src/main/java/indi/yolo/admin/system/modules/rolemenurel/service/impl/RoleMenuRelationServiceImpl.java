package indi.yolo.admin.system.modules.rolemenurel.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import indi.yolo.admin.system.modules.rolemenurel.entity.RoleMenuRelation;
import indi.yolo.admin.system.modules.rolemenurel.entity.table.RoleMenuRelationTableDef;
import indi.yolo.admin.system.modules.rolemenurel.mapper.RoleMenuRelationMapper;
import indi.yolo.admin.system.modules.rolemenurel.service.IRoleMenuRelationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author yoloz
 */
@Service
public class RoleMenuRelationServiceImpl extends ServiceImpl<RoleMenuRelationMapper, RoleMenuRelation> implements IRoleMenuRelationService {

    @Resource
    private RoleMenuRelationMapper roleMenuRelationMapper;

    @Override
    public int addRoleMenuRelation(Integer roleId, Collection<Integer> menuIds) {
        List<RoleMenuRelation> list = new ArrayList<>(menuIds.size());
        for (Integer menuId : menuIds) {
            RoleMenuRelation roleMenuRelation = new RoleMenuRelation(roleId, menuId);
            list.add(roleMenuRelation);
        }
        return roleMenuRelationMapper.insertBatch(list);
    }

    @Override
    public int delRoleMenuRelationByRole(Integer roleId) {
        QueryWrapper sql = QueryWrapper.create().where(RoleMenuRelationTableDef.ROLE_MENU_RELATION.ROLE_ID.eq(roleId));
        return roleMenuRelationMapper.deleteByQuery(sql);
    }

    @Override
    public Collection<Integer> getMenuIdsByRole(Integer roleId) {
        QueryWrapper sql = QueryWrapper.create()
                .select(RoleMenuRelationTableDef.ROLE_MENU_RELATION.MENU_ID)
                .from(RoleMenuRelationTableDef.ROLE_MENU_RELATION)
                .where(RoleMenuRelationTableDef.ROLE_MENU_RELATION.ROLE_ID.eq(roleId));
        return roleMenuRelationMapper.selectListByQueryAs(sql, Integer.class);
    }
}
