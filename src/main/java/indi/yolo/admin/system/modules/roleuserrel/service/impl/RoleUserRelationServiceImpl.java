package indi.yolo.admin.system.modules.roleuserrel.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import indi.yolo.admin.system.modules.role.entity.table.RoleTableDef;
import indi.yolo.admin.system.modules.roleuserrel.entity.RoleUserRelation;
import indi.yolo.admin.system.modules.roleuserrel.entity.RoleUserRelationDTO;
import indi.yolo.admin.system.modules.roleuserrel.entity.table.RoleUserRelationTableDef;
import indi.yolo.admin.system.modules.roleuserrel.mapper.RoleUserRelationMapper;
import indi.yolo.admin.system.modules.roleuserrel.service.IRoleUserRelationService;
import indi.yolo.admin.system.modules.user.entity.User;
import indi.yolo.admin.system.modules.user.entity.table.UserTableDef;
import indi.yolo.admin.system.modules.user.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author yoloz
 */
@Service
public class RoleUserRelationServiceImpl extends ServiceImpl<RoleUserRelationMapper, RoleUserRelation> implements IRoleUserRelationService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleUserRelationMapper userRoleRelationMapper;

    @Override
    public int addUserRoleRelation(Integer userId, Collection<Integer> roleIds) {
        List<RoleUserRelation> list = new ArrayList<>(roleIds.size());
        for (Integer roleId : roleIds) {
            RoleUserRelation userRoleRelation = new RoleUserRelation(userId, roleId);
            list.add(userRoleRelation);
        }
        return userRoleRelationMapper.insertBatch(list);
    }

    @Override
    public Page<User> getUserByRole(RoleUserRelationDTO roleUserRelationDTO) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(UserTableDef.USER.ALL_COLUMNS)
                .from(UserTableDef.USER)
                .where(UserTableDef.USER.ID.in(
                        QueryWrapper.create().select(RoleUserRelationTableDef.ROLE_USER_RELATION.USER_ID)
                                .from(RoleUserRelationTableDef.ROLE_USER_RELATION, RoleTableDef.ROLE)
                                .where(RoleUserRelationTableDef.ROLE_USER_RELATION.ROLE_ID.eq(RoleTableDef.ROLE.ID))
                                .and(RoleTableDef.ROLE.ID.eq(roleUserRelationDTO.getId()))
                ))
                .orderBy(UserTableDef.USER.ID.desc());
        return userMapper.paginate(Page.of(roleUserRelationDTO.getPageNumber(), roleUserRelationDTO.getPageSize(),
                        roleUserRelationDTO.getTotalRow()), queryWrapper)
                .map(u -> {
                    u.setPassword(null);
                    return u;
                });
    }

    @Override
    public int delRoleUserRelation(Integer roleId, Collection<Integer> userIds) {
        QueryWrapper sql = QueryWrapper.create()
                .where(RoleUserRelationTableDef.ROLE_USER_RELATION.ROLE_ID.eq(roleId))
                .and(RoleUserRelationTableDef.ROLE_USER_RELATION.USER_ID.in(userIds));
        return userRoleRelationMapper.deleteByQuery(sql);
    }

    @Override
    public int delRoleUserRelationByUser(Integer userId) {
        QueryWrapper sql = QueryWrapper.create()
                .where(RoleUserRelationTableDef.ROLE_USER_RELATION.USER_ID.eq(userId));
        return userRoleRelationMapper.deleteByQuery(sql);
    }
}
