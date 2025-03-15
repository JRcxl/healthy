package com.itheima.config;

// 核心配置类注解
import org.springframework.context.annotation.Configuration;

// 启用异步支持注解
import org.springframework.scheduling.annotation.EnableAsync;

// 异步配置接口（定义线程池和异常处理器）
import org.springframework.scheduling.annotation.AsyncConfigurer;

// Java 并发执行器接口
import java.util.concurrent.Executor;

// Spring 线程池任务执行器实现类
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.initialize();
        return executor;
    }
}