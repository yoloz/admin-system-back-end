package indi.yolo.admin.system.modules.log.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 1. syslog(系统审计)无需创建者或者更新者;
 * 2. 保存是async,BasePOListener中获取getRequestAttributes(绑定线程上)是null
 *
 * @author yoloz
 */
@Data
@Table("log")
@NoArgsConstructor
public class SysLog {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private String username;

    private String annotation; // 注解信息(功能说明)

    private String method;

    private String params;

    private String logType; // log_type

    private String requestIp; // request_ip

    private String address;

    private String browser;

    private Long costTime; // cost_time

    @Column(isLarge = true)
    private String exception;

    private Timestamp createTime;

    public SysLog(String logType, Long time) {
        this.logType = logType;
        this.costTime = time;
    }
}
