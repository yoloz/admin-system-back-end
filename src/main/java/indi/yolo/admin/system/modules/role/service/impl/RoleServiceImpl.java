package indi.yolo.admin.system.modules.role.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import indi.yolo.admin.system.modules.role.entity.Role;
import indi.yolo.admin.system.modules.role.entity.RoleDTO;
import indi.yolo.admin.system.modules.role.entity.table.RoleTableDef;
import indi.yolo.admin.system.modules.role.mapper.RoleMapper;
import indi.yolo.admin.system.modules.role.mapstruct.RoleMapStruct;
import indi.yolo.admin.system.modules.role.service.IRoleService;
import indi.yolo.admin.system.modules.roleuserrel.entity.table.RoleUserRelationTableDef;
import indi.yolo.admin.system.modules.user.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.min;
import static com.mybatisflex.core.query.QueryMethods.select;

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
        QueryWrapper sql = QueryWrapper.create().select(RoleTableDef.ROLE.DEFAULT_COLUMNS).from(RoleTableDef.ROLE)
                .where(RoleTableDef.ROLE.NAME.like(roleDTO.getName()));
        return roleMapper.paginate(Page.of(roleDTO.getPageNumber(), roleDTO.getPageSize(), roleDTO.getTotalRow()), sql);
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
        return UpdateChain.create(roleMapper).set(RoleTableDef.ROLE.NAME, roleDTO.getName())
                .set(RoleTableDef.ROLE.LEVEL, roleDTO.getLevel())
                .set(RoleTableDef.ROLE.DESC, roleDTO.getDesc())
                .where(RoleTableDef.ROLE.ID.eq(roleDTO.getId())).update();
    }

    @Override
    public Integer removeRole(Collection<Integer> roleIds) {
        return roleMapper.deleteBatchByIds(roleIds);
    }

    @Override
    public List<Role> getRoleOptionByUser(Integer userId) {
        QueryWrapper sql = QueryWrapper.create()
                .select(RoleTableDef.ROLE.ID, RoleTableDef.ROLE.NAME)
                .from(RoleTableDef.ROLE)
                .where(RoleTableDef.ROLE.LEVEL.ge(
                        select(min(RoleTableDef.ROLE.LEVEL)).from(RoleTableDef.ROLE).where(RoleTableDef.ROLE.ID.in(
                                select(RoleUserRelationTableDef.ROLE_USER_RELATION.ROLE_ID)
                                        .from(RoleUserRelationTableDef.ROLE_USER_RELATION)
                                        .where(RoleUserRelationTableDef.ROLE_USER_RELATION.USER_ID.eq(userId))
                        ))
                ));
        return roleMapper.selectListByQuery(sql);
    }
}
