package indi.yolo.admin.system.modules.user.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import indi.yolo.admin.system.modules.user.entity.User;
import indi.yolo.admin.system.modules.user.mapper.UserMapper;
import indi.yolo.admin.system.modules.user.service.ILoginService;
import indi.yolo.admin.system.modules.user.service.IUserService;
import indi.yolo.admin.system.modules.user.entity.table.UserTableDef;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * @author yoloz
 */
@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements ILoginService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private IUserService userService;

    @Override
    public Integer login(String username, String passwd) {
//        String pwd = Utils.ripeMD160(passwd);
        QueryWrapper sql = QueryWrapper.create()
                .select(UserTableDef.USER.ID).from(UserTableDef.USER)
                .where(UserTableDef.USER.USERNAME.eq(username))
                .and(UserTableDef.USER.PASSWORD.eq(passwd))
                .and(UserTableDef.USER.ENABLE.eq(true));
        User user = getOne(sql);
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    @Override
    public boolean updateLoginInfo(String loginIp, Integer userId) {
        Timestamp loginTime = new Timestamp(System.currentTimeMillis());
        return UpdateChain.create(userMapper)
                .set(UserTableDef.USER.LOGIN_IP, loginIp)
                .set(UserTableDef.USER.LOGIN_TIME, loginTime)
                .where(UserTableDef.USER.ID.eq(userId))
                .update();
    }
}
