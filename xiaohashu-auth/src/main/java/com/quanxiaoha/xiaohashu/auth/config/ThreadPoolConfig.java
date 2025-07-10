package com.quanxiaoha.xiaohashu.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        threadPoolTaskExecutor.setCorePoolSize(5);
        //最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(50);
        //队列容量
        threadPoolTaskExecutor.setQueueCapacity(200);
        //线程活跃时间（以秒为单位）
        threadPoolTaskExecutor.setKeepAliveSeconds(30);
        //线程名前缀
        threadPoolTaskExecutor.setThreadNamePrefix("AuthExecutor-");
        //拒绝策略
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待所有任务结束后再关闭线程池
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);

        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }
}
