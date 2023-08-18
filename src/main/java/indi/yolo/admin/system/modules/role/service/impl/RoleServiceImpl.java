package indi.yolo.admin.system.modules.role.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import indi.yolo.admin.system.modules.role.entity.Role;
import indi.yolo.admin.system.modules.role.entity.RoleDTO;
import indi.yolo.admin.system.modules.role.entity.RoleUserRelationDTO;
import indi.yolo.admin.system.modules.role.mapper.RoleMapper;
import indi.yolo.admin.system.modules.role.mapstruct.RoleMapStruct;
import indi.yolo.admin.system.modules.role.service.IRoleService;
import indi.yolo.admin.system.modules.user.entity.User;
import indi.yolo.admin.system.modules.user.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.min;
import static com.mybatisflex.core.query.QueryMethods.select;
import static indi.yolo.admin.system.modules.role.entity.table.RoleTableDef.ROLE;
import static indi.yolo.admin.system.modules.user.entity.table.UserRoleRelationTableDef.USER_ROLE_RELATION;
import static indi.yolo.admin.system.modules.user.entity.table.UserTableDef.USER;

/**
 * @author yoloz
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    RoleMapper roleMapper;
    @Resource
    UserMapper userMapper;

    @Autowired
    private RoleMapStruct roleMapStruct;

    @Override
    public Page<Role> getRoleList(RoleDTO roleDTO) {
        QueryWrapper sql = QueryWrapper.create().select(ROLE.DEFAULT_COLUMNS).from(ROLE)
                .where(ROLE.NAME.like(roleDTO.getName()));
        return roleMapper.paginate(Page.of(roleDTO.getPageNumber(), roleDTO.getPageSize(), roleDTO.getTotalRow()), sql);
    }

    @Override
    public Page<User> getUserByRole(RoleUserRelationDTO roleUserRelationDTO) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(USER.ALL_COLUMNS)
                .from(USER)
                .where(USER.ID.in(
                        QueryWrapper.create().select(USER_ROLE_RELATION.USER_ID).from(USER_ROLE_RELATION)
                                .leftJoin(ROLE).on(USER_ROLE_RELATION.ROLE_ID.eq(ROLE.ID))
                                .and(ROLE.ID.eq(roleUserRelationDTO.getId()))
                ));
        return userMapper.paginate(Page.of(roleUserRelationDTO.getPageNumber(), roleUserRelationDTO.getPageSize(),
                        roleUserRelationDTO.getTotalRow()), queryWrapper)
                .map(u -> {
                    u.setPassword(null);
                    return u;
                });
    }

    @Override
    public Integer addRole(RoleDTO roleDTO) {
        Role role = roleMapStruct.toEntity(roleDTO);
        return roleMapper.insertSelective(role);
    }

    @Override
    public boolean updateRole(RoleDTO roleDTO) {
//        Role role = roleMapStruct.toEntity(roleDTO);
//        return roleMapper.update(role, true);
        // 避免更新变更builtin，手动组装SQL语句
        return UpdateChain.create(roleMapper).set(ROLE.NAME, roleDTO.getName()).set(ROLE.LEVEL, roleDTO.getLevel())
                .set(ROLE.DESC, roleDTO.getDesc())
                .where(ROLE.ID.eq(roleDTO.getId())).update();
    }

    @Override
    public Integer removeRole(Collection<Integer> roleIds) {
        return roleMapper.deleteBatchByIds(roleIds);
    }

    @Override
    public List<Role> getRoleOptionByUser(Integer userId) {
        QueryWrapper sql = QueryWrapper.create()
                .select(ROLE.ID, ROLE.NAME)
                .from(ROLE)
                .where(ROLE.LEVEL.ge(
                        select(min(ROLE.LEVEL)).from(ROLE).where(ROLE.ID.in(
                                select(USER_ROLE_RELATION.ROLE_ID).from(USER_ROLE_RELATION)
                                        .where(USER_ROLE_RELATION.USER_ID.eq(userId))
                        ))
                ));
        return roleMapper.selectListByQuery(sql);
    }

//    @Override
//    public List<Role> getRoleByUser(Integer userId) {
//        QueryWrapper sql = QueryWrapper.create()
//                .select(ROLE.ID, ROLE.NAME, ROLE.LEVEL)
//                .from(ROLE)
//                .where(ROLE.ID.in(
//                        select(USER_ROLE_RELATION.ROLE_ID).from(USER_ROLE_RELATION)
//                                .where(USER_ROLE_RELATION.USER_ID.eq(userId))
//                ));
//        return roleMapper.selectListByQuery(sql);
//    }
}
