package indi.yolo.admin.system.modules.menu.entity;

import lombok.Data;

import java.util.Collection;

/**
 * @author yoloz
 */
@Data
public class MenuVO {
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
    private Collection<MenuVO> children;
}
