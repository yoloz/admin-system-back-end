package indi.yolo.admin.system.modules.log.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import indi.yolo.admin.system.modules.log.entity.SysLog;
import indi.yolo.admin.system.modules.log.entity.LogDTO;
import org.aspectj.lang.JoinPoint;
import org.springframework.scheduling.annotation.Async;

/**
 * @author yoloz
 */
public interface ILogService extends IService<SysLog> {

    Page<SysLog> getLogList(LogDTO logListDto);

    @Async
    void save(String username, String browser, String ip, JoinPoint joinPoint, SysLog log);

    String getErrDetail(Long id);

}
