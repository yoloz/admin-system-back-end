package indi.yolo.admin.system.modules.log.entity;

import indi.yolo.admin.system.commons.entity.base.BaseDTO;
import lombok.Data;

/**
 * @author yoloz
 */
@Data
public class LogDTO extends BaseDTO {

    private String username;
    private String requestIp;
    private String beginTime;
    private String endTime;
    private String logType;

}
