package indi.yolo.admin.system.modules.menu.entity;

import indi.yolo.admin.system.commons.entity.base.BaseDTO;
import lombok.Data;

/**
 * @author yoloz
 */
@Data
public class MenuDTO extends BaseDTO {
    private Integer id;
    private String name;
    private String path;
    private String redirect;
    private String component;
    private Integer pid;
    private Integer type; // 0:目录 1:菜单 2:按钮
    private String icon;
    private boolean hidden;
    private Integer order;
    private String permission;

}
