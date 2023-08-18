package indi.yolo.admin.system.container;

import indi.yolo.admin.system.commons.utils.JsonUtil;
import indi.yolo.admin.system.modules.menu.entity.MenuRouter;
import indi.yolo.admin.system.modules.menu.service.IMenuService;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 路径 ws://localhost:8087/ws
 *
 * @author yoloz
 */
@Slf4j
@Component
@ServerEndpoint("/ws")
public class WebSocket {

    private Session session;

    private static IMenuService menuService;

    @Autowired
    public void setMenuService(IMenuService ms) {
        menuService = ms;
    }

    //连接建立
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    //连接断开
    @OnClose
    public void onClose() {
        try {
            if (session != null) session.close();
        } catch (IOException e) {
            log.warn("close ws session fail:" + e.getMessage());
        }
    }

    //收到客户端消息
    @OnMessage
    public void onMessage(String message) {
        if (StringUtils.isEmpty(message)) return;
        if (message.startsWith("routerMenu")) {
            String[] s = message.split("_");
            Collection<MenuRouter> routerMenus = menuService.getRouterMenu(Integer.parseInt(s[1]));
            Map<String, Object> menus = new HashMap<>(2);
            menus.put("type", "routerMenu");
            menus.put("data", routerMenus);
            sendMsg(JsonUtil.obj2String(menus));
        }
    }

    //发送错误时的处理
    @OnError
    public void onError(Session session, Throwable e) {
        log.error("WS Connect Error:" + e.getMessage(), e);
        try {
            session.close();
        } catch (IOException ignore) {
        }
    }

    public void sendMsg(String message) {
        if (session != null && session.isOpen()) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.warn("send msg fail:" + e.getMessage());
            }
        }
    }

}
