package indi.yolo.admin.system.modules.user.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.service.IService;
import indi.yolo.admin.system.modules.user.entity.User;
import indi.yolo.admin.system.modules.user.entity.UserDTO;
import indi.yolo.admin.system.modules.user.entity.UserVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;
import java.util.Optional;

/**
 * @author yoloz
 */
public interface IUserService extends IService<User> {

    Page<UserVO> getUserList(UserDTO userDto);

    Integer getIdByNameAndPwd(String username, String passwd);

    //返回插入的自增ID
    Integer addUser(UserDTO userDTO);

    // 仅更新用户信息，相关角色信息其他接口
    boolean updateUser(UserDTO userDTO);

    @CacheEvict(cacheNames = "permissions", allEntries = true)
    Integer removeUser(Collection<Integer> userIds);

    boolean updatePwd(Integer userId, String passwd);

    @CacheEvict(cacheNames = "permissions", key = "#userId")
    boolean changeEnable(Integer userId, Boolean enable);

    Optional<Row> getUserInfo(Integer userId);

    @Cacheable(cacheNames = "permissions", key = "#userId")
    Collection<String> getPermission(Integer userId);
}
