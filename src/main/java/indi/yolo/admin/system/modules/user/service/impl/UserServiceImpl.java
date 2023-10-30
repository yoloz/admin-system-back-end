package indi.yolo.admin.system.modules.user.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import indi.yolo.admin.system.modules.menu.entity.table.MenuTableDef;
import indi.yolo.admin.system.modules.role.entity.table.RoleTableDef;
import indi.yolo.admin.system.modules.rolemenurel.entity.table.RoleMenuRelationTableDef;
import indi.yolo.admin.system.modules.roleuserrel.entity.table.RoleUserRelationTableDef;
import indi.yolo.admin.system.modules.user.entity.User;
import indi.yolo.admin.system.modules.user.entity.UserDTO;
import indi.yolo.admin.system.modules.user.entity.UserVO;
import indi.yolo.admin.system.modules.user.entity.table.UserTableDef;
import indi.yolo.admin.system.modules.user.mapper.UserMapper;
import indi.yolo.admin.system.modules.user.mapstruct.UserMapStruct;
import indi.yolo.admin.system.modules.user.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.mybatisflex.core.query.QueryMethods.select;

/**
 * @author yoloz
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserMapStruct userMapStruct;

    @Override
    public Page<UserVO> getUserList(UserDTO userDto) {
        QueryWrapper sql = QueryWrapper.create()
                .select(UserTableDef.USER.ALL_COLUMNS, RoleTableDef.ROLE.ID, RoleTableDef.ROLE.NAME, RoleTableDef.ROLE.LEVEL)
                .from(UserTableDef.USER.as("u"))
                .leftJoin(RoleUserRelationTableDef.ROLE_USER_RELATION).as("ur")
                .on(RoleUserRelationTableDef.ROLE_USER_RELATION.USER_ID.eq(UserTableDef.USER.ID))
                .and(UserTableDef.USER.USERNAME.like(userDto.getUsername())).and(UserTableDef.USER.ENABLE.eq(userDto.getEnable()))
                .leftJoin(RoleTableDef.ROLE).as("r").on(RoleUserRelationTableDef.ROLE_USER_RELATION.ROLE_ID.eq(RoleTableDef.ROLE.ID));
        return userMapper.paginateAs(Page.of(userDto.getPageNumber(), userDto.getPageSize(), userDto.getTotalRow()), sql, UserVO.class);
    }

    @Override
    public Integer getIdByNameAndPwd(String username, String passwd) {
//        String pwd = Utils.ripeMD160(passwd);
        QueryWrapper sql = QueryWrapper.create()
                .select(UserTableDef.USER.ID).from(UserTableDef.USER)
                .where(UserTableDef.USER.USERNAME.eq(username))
                .and(UserTableDef.USER.PASSWORD.eq(passwd));
        User user = getOne(sql);
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    @Override
    public Integer addUser(UserDTO userDTO) {
        User user = userMapStruct.toEntity(userDTO);
        userMapper.insertSelective(user);
        return user.getId();
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        return UpdateChain.create(userMapper).set(UserTableDef.USER.NICKNAME, userDTO.getNickname())
                .set(UserTableDef.USER.PHONE, userDTO.getPhone())
                .set(UserTableDef.USER.EMAIL, userDTO.getEmail())
                .where(UserTableDef.USER.ID.eq(userDTO.getId())).update();
    }

    @Override
    public Integer removeUser(Collection<Integer> userIds) {
        return userMapper.deleteBatchByIds(userIds);
    }

    @Override
    public boolean updatePwd(Integer userId, String passwd) {
        return UpdateChain.create(userMapper)
                .set(UserTableDef.USER.PASSWORD, passwd)
                .where(UserTableDef.USER.ID.eq(userId))
                .update();
    }

    @Override
    public boolean changeEnable(Integer userId, Boolean enable) {
        return UpdateChain.create(userMapper)
                .set(UserTableDef.USER.ENABLE, enable)
                .where(UserTableDef.USER.ID.eq(userId))
                .update();
    }

    @Override
    public Optional<Row> getUserInfo(Integer userId) {
        QueryWrapper sql = QueryWrapper.create()
                .select(UserTableDef.USER.ID, UserTableDef.USER.USERNAME, UserTableDef.USER.NICKNAME,
                        UserTableDef.USER.PHONE, UserTableDef.USER.EMAIL, UserTableDef.USER.ENABLE,
                        RoleTableDef.ROLE.LEVEL).from(UserTableDef.USER.as("u"))
                .leftJoin(RoleUserRelationTableDef.ROLE_USER_RELATION).as("ur")
                .on(RoleUserRelationTableDef.ROLE_USER_RELATION.USER_ID.eq(UserTableDef.USER.ID))
                .and(UserTableDef.USER.ID.eq(userId))
                .leftJoin(RoleTableDef.ROLE).as("r")
                .on(RoleUserRelationTableDef.ROLE_USER_RELATION.ROLE_ID.eq(RoleTableDef.ROLE.ID));
        List<Row> list = Db.selectListByQuery(sql);
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        return list.stream().min((o1, o2) -> {
            int x = o1.getInt("level");
            int y = o2.getInt("level");
            return Integer.compare(x, y);
        });
    }

    @Override
    public Collection<String> getPermission(Integer userId) {
        QueryWrapper sql = QueryWrapper.create().select(QueryMethods.distinct(MenuTableDef.MENU.PERMISSION))
                .from(MenuTableDef.MENU)
                .where(MenuTableDef.MENU.ID.in(
                        select(QueryMethods.distinct(RoleMenuRelationTableDef.ROLE_MENU_RELATION.MENU_ID))
                                .from(RoleMenuRelationTableDef.ROLE_MENU_RELATION)
                                .where(RoleMenuRelationTableDef.ROLE_MENU_RELATION.ROLE_ID.in(
                                        QueryMethods.select(RoleUserRelationTableDef.ROLE_USER_RELATION.ROLE_ID)
                                                .from(RoleUserRelationTableDef.ROLE_USER_RELATION)
                                                .where(RoleUserRelationTableDef.ROLE_USER_RELATION.USER_ID.eq(userId))
                                ))
                ));
        return userMapper.selectListByQueryAs(sql, String.class);
    }
}
