package indi.yolo.admin.system.modules.menu.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import indi.yolo.admin.system.commons.entity.base.BasePO;
import lombok.Data;


/**
 * @author yoloz
 */
@Data
@Table(value = "menu")
public class Menu {

    @Id(keyType = KeyType.Auto)
    private Integer id;
    private Integer pid; // 上级组件ID
    private Integer type; // 0:目录 1:菜单 2:按钮
    private String icon; // 组件图标
    private boolean hidden; // 是否隐藏
    private Integer order; // 组件排序
    private String permission; // 权限标识
    private String name; // 组件名称
    private String path; // 路由地址
    private String redirect;
    private String component; //组件路径
}
