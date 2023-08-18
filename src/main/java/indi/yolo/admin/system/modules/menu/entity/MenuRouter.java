package indi.yolo.admin.system.modules.menu.entity;

import lombok.Data;
import lombok.Getter;

import java.util.Collection;

/**
 * @author yoloz
 */
@Data
public class MenuRouter {
    private Integer id;
    private String name;
    private String path;
    private String redirect;
    private String component;
    private Meta meta;
    private Collection<MenuRouter> children;

    @Getter
    public static class Meta {
        private final String icon; // 组件图标
        private final boolean hidden; // 是否隐藏

        public Meta(String icon, boolean hidden) {
            this.icon = icon;
            this.hidden = hidden;
        }
    }
}
