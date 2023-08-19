package indi.yolo.admin.system.modules.user.service;

import com.mybatisflex.core.service.IService;
import indi.yolo.admin.system.modules.user.entity.User;


/**
 * @author yoloz
 */
public interface ILoginService extends IService<User> {

    Integer login(String username, String password);

    @SuppressWarnings("all")
    boolean updateLoginInfo(String loginIp, Integer userId);

}
