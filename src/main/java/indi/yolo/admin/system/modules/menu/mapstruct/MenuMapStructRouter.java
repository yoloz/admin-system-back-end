package indi.yolo.admin.system.modules.menu.mapstruct;

import indi.yolo.admin.system.modules.menu.entity.Menu;
import indi.yolo.admin.system.modules.menu.entity.MenuRouter;
import org.springframework.stereotype.Component;

/**
 * @author yoloz
 */
@Component
public class MenuMapStructRouter {

    public MenuRouter toRouter(Menu menu) {
        MenuRouter routerMenu = new MenuRouter();
        routerMenu.setId(menu.getId());
        routerMenu.setName(menu.getName());
        routerMenu.setPath(menu.getPath());
        routerMenu.setRedirect(menu.getRedirect());
        routerMenu.setComponent(menu.getComponent());
        MenuRouter.Meta meta = new MenuRouter.Meta(menu.getIcon(), menu.isHidden());
        routerMenu.setMeta(meta);
        return routerMenu;
    }
}
