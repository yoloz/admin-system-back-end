package indi.yolo.admin.system.modules.menu.controller;

import indi.yolo.admin.system.commons.entity.rest.RestResult;
import indi.yolo.admin.system.modules.log.annotation.Log;
import indi.yolo.admin.system.modules.menu.entity.MenuDTO;
import indi.yolo.admin.system.modules.menu.entity.MenuRouter;
import indi.yolo.admin.system.modules.menu.entity.MenuVO;
import indi.yolo.admin.system.modules.menu.service.IMenuService;
import indi.yolo.admin.system.commons.entity.Permission;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Permission("menu:list")
    @Log("查询菜单列表")
    @PostMapping(value = "/list")
    public RestResult<Object> getMenuList(MenuDTO menuDTO) {
        Collection<MenuVO> menuVOS = menuService.getMenuList(menuDTO);
        return RestResult.success(menuVOS);
    }

    @GetMapping(value = "/sOptions")
    public RestResult<Object> selectOptions() {
        Collection<MenuVO> menuVOS = menuService.selectOptions();
        MenuVO menuVO = new MenuVO();
        menuVO.setId(0);
        menuVO.setName("一级目录");
        menuVO.setChildren(menuVOS);
        return RestResult.success(List.of(menuVO));
    }

    @PostMapping(value = "/getRouterMenus")
    public RestResult<Object> getRouterMenus(@RequestBody Integer userId) {
        Collection<MenuRouter> routerMenus = menuService.getRouterMenu(userId);
        return RestResult.success(routerMenus);
    }

    @Permission("menu:add")
    @Log("新增菜单")
    @PostMapping(value = "/add")
    public RestResult<Object> addMenu(@RequestBody MenuDTO menuDTO) {
        int i = menuService.addMenu(menuDTO);
        if (i == 0) {
            return RestResult.error("新增失败!");
        }
        return RestResult.success(true);
    }

    @Permission("menu:edit")
    @Log("编辑菜单")
    @PostMapping(value = "/edit")
    public RestResult<Object> updateMenu(@RequestBody MenuDTO menuDTO) {
        int i = menuService.updateMenu(menuDTO);
        if (i == 0) {
            return RestResult.error("编辑失败!");
        }
        return RestResult.success(true);
    }

    @Permission("menu:delete")
    @Log("删除菜单")
    @PostMapping(value = "/delete")
    public RestResult<Object> removeMenu(@RequestBody Collection<Integer> ids) {
        int i = menuService.removeMenu(ids);
        if (i == 0) {
            return RestResult.error("删除失败!");
        }
        return RestResult.success(true);
    }

}
