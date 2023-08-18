package indi.yolo.admin.system.modules.log.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import indi.yolo.admin.system.modules.log.entity.SysLog;
import indi.yolo.admin.system.modules.log.entity.LogDTO;
import indi.yolo.admin.system.modules.log.mapper.LogMapper;
import indi.yolo.admin.system.modules.log.service.ILogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;

import static indi.yolo.admin.system.modules.log.entity.table.SysLogTableDef.SYS_LOG;

/**
 * @author yoloz
 */
@Slf4j
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, SysLog> implements ILogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public Page<SysLog> getLogList(LogDTO logDto) {
        QueryWrapper sql = QueryWrapper.create()
                .select(SYS_LOG.DEFAULT_COLUMNS) //不查询大字段exception
                .where(SYS_LOG.LOG_TYPE.eq(logDto.getLogType()))
                .and(SYS_LOG.USERNAME.like(logDto.getUsername()))
                .and(SYS_LOG.REQUEST_IP.like(logDto.getRequestIp()))
                .and(SYS_LOG.CREATE_TIME.ge(logDto.getBeginTime()))
                .and(SYS_LOG.CREATE_TIME.le(logDto.getEndTime()));
        return page(Page.of(logDto.getPageNumber(), logDto.getPageSize(), logDto.getTotalRow()), sql);
    }

    @Override
    public void save(String username, String browser, String ip, JoinPoint joinPoint, SysLog syslog) {
        if (syslog == null) {
            log.warn("operation log is null...");
            return;
        }
        logMapper.insertSelective(syslog);
    }

    @Override
    public String getErrDetail(Long id) {
        QueryWrapper sql = QueryWrapper.create().select(SYS_LOG.EXCEPTION).where(SYS_LOG.ID.eq(id));
        return getOne(sql).getException();
    }

}
