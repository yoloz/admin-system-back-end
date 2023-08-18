package indi.yolo.admin.system.modules.user.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import indi.yolo.admin.system.modules.user.entity.UserRoleRelation;
import indi.yolo.admin.system.modules.user.mapper.UserRoleRelationMapper;
import indi.yolo.admin.system.modules.user.service.IUserRoleRelationService;
import indi.yolo.admin.system.modules.user.entity.table.UserRoleRelationTableDef;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author yoloz
 */
@Service
public class UserRoleRelationServiceImpl extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation> implements IUserRoleRelationService {

    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;

    @Override
    public int addUserRoleRelation(Integer userId, Collection<Integer> roleIds) {
        List<UserRoleRelation> list = new ArrayList<>(roleIds.size());
        for (Integer roleId : roleIds) {
            UserRoleRelation userRoleRelation = new UserRoleRelation(userId, roleId);
            list.add(userRoleRelation);
        }
        return userRoleRelationMapper.insertBatch(list);
    }

    @Override
    public int delRoleUserRelation(Integer roleId, Collection<Integer> userIds) {
        QueryWrapper sql = QueryWrapper.create()
                .where(UserRoleRelationTableDef.USER_ROLE_RELATION.ROLE_ID.eq(roleId))
                .and(UserRoleRelationTableDef.USER_ROLE_RELATION.USER_ID.in(userIds));
        return userRoleRelationMapper.deleteByQuery(sql);
    }

    @Override
    public int delRoleUserRelationByUser(Integer userId) {
        QueryWrapper sql = QueryWrapper.create()
                .where(UserRoleRelationTableDef.USER_ROLE_RELATION.USER_ID.eq(userId));
        return userRoleRelationMapper.deleteByQuery(sql);
    }
}
