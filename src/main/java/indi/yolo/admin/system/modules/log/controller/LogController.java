package indi.yolo.admin.system.modules.log.controller;

import indi.yolo.admin.system.commons.entity.rest.RestResult;
import indi.yolo.admin.system.modules.log.annotation.Log;
import indi.yolo.admin.system.modules.log.entity.LogDTO;
import indi.yolo.admin.system.modules.log.service.ILogService;
import indi.yolo.admin.system.commons.entity.Permission;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

/**
 * @author yoloz
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private ILogService logService;

    @Permission({"log:right", "log:error"})
    @Log("操作日志列表")
    @PostMapping(value = "/list")
    public RestResult<Object> getLogList(@RequestBody LogDTO logDto) {
        return RestResult.success(logService.getLogList(logDto));
    }

    @Permission("log:error")
    @Log("操作日志详情")
    @PostMapping(value = "/exception")
    public RestResult<Object> getErrDetail(@RequestBody Map<String, Long> id) {
        if (id == null || id.isEmpty()) {
            return RestResult.error("日志ID不存在!");
        }
        return RestResult.success(logService.getErrDetail(id.get("id")));
    }

    @Permission("log:delete")
    @Log("操作日志删除")
    @PostMapping(value = "/delete")
    public RestResult<Object> deleteLog(@RequestBody Collection<Long> ids) {
        return RestResult.success(logService.removeByIds(ids));
    }

    @Permission("log:export")
    @Log("操作日志导出")
    @PostMapping(value = "/export")
    public RestResult<Object> export(@RequestBody LogDTO logDto) {
        //todo
        return RestResult.success("");
    }

}
