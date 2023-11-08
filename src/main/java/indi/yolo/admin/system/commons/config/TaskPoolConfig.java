package indi.yolo.admin.system.commons.config;

import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yolo
 */
@Configuration
public class TaskPoolConfig {

    @Bean(name = AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); //核心线程池大小
        executor.setMaxPoolSize(10); //最大线程数
        executor.setQueueCapacity(200); //队列容量
        executor.setKeepAliveSeconds(60); //活跃时间
        executor.setThreadNamePrefix("admin-system-"); //线程名字前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //哪个线程调用了execute方法，那么这个线程来执行被拒绝的任务
        return executor;
    }
}
