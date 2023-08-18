package indi.yolo.admin.system.modules.user.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import indi.yolo.admin.system.commons.entity.base.BasePO;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author yoloz
 */
@Data
@Table(value = "user")
public class User extends BasePO {

    @Id(keyType = KeyType.Auto)
    private Integer id;
    private String nickname;
    //unique index
    private String username;
    private String password;
    // 启用 or 停用
    private boolean enable;
    // 内置用户
    @Column(onInsertValue = "false")
    private boolean builtin;
    private String phone;
    private String email;
    // 登陆的时候更新
    private String loginIp;
    private Timestamp loginTime;
}
