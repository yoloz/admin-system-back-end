package indi.yolo.admin.system.modules.user.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import indi.yolo.admin.system.container.AppApplication;
import indi.yolo.admin.system.modules.user.entity.UserDTO;
import indi.yolo.admin.system.modules.user.mapper.UserMapper;
import indi.yolo.admin.system.modules.user.entity.table.UserTableDef;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AppApplication.class)
class UserServiceImplTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void getUserList() {
        UserDTO userDto = new UserDTO();
        userDto.setUsername("test");
        QueryWrapper sql = QueryWrapper.create().select(UserTableDef.USER.DEFAULT_COLUMNS).from(UserTableDef.USER)
                .where(UserTableDef.USER.USERNAME.like(userDto.getUsername()))
                .and(UserTableDef.USER.ENABLE.eq(userDto.getEnable()));
        System.out.println(sql.toSQL());
    }

    @Test
    void getIdByNameAndPwd() {
        QueryWrapper sql = QueryWrapper.create().select(UserTableDef.USER.ID).from(UserTableDef.USER)
                .where(UserTableDef.USER.USERNAME.eq("username"))
                .and(UserTableDef.USER.PASSWORD.eq("passwd"));
        System.out.println(sql.toSQL());
    }

    @Test
    void updateLoginInfo() {

    }
}