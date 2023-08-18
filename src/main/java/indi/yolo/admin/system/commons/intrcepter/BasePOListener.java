package indi.yolo.admin.system.commons.intrcepter;

import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import indi.yolo.admin.system.commons.config.UserContextManager;
import indi.yolo.admin.system.commons.entity.base.BasePO;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

/**
 * @author yoloz
 */
@AllArgsConstructor
public class BasePOListener implements InsertListener, UpdateListener {

    private final UserContextManager userContextManager;

    @Override
    public void onInsert(Object o) {
        BasePO basePO = (BasePO) o;
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String username = userContextManager.getUserName();
//        basePO.setCreateTime(timestamp);
        basePO.setCreateUser(username);
    }

    @Override
    public void onUpdate(Object o) {
        BasePO basePO = (BasePO) o;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String username = userContextManager.getUserName();
        basePO.setUpdateTime(timestamp);
        basePO.setUpdateUser(username);
    }
}
