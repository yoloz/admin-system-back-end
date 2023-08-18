package indi.yolo.admin.system.commons.entity.base;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author yoloz
 */
@Data
public class BasePO {

    private String createUser; // create_user
    private Timestamp createTime; // create_time
    private String updateUser; // update_user
    private Timestamp updateTime; // update_time
}
