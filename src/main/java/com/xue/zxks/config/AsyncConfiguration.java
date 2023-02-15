package com.xue.zxks.config;

import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

@Configuration
@Slf4j
public class AsyncConfiguration implements AsyncConfigurer {

    @Value("${async.core-pool-size}")
    private int corePoolSize;

    @Value("${async.maximum-pool-size}")
    private int maximumPoolSize;

    @Value("${async.keep-alive-time}")

    private int keepAliveTime;
    @Override
    public Executor getAsyncExecutor() {
        log.info(
            "core-pool-size: {}, maximum-pool-size: {}, keep-alive-time: {}",
            corePoolSize,
            maximumPoolSize,
            keepAliveTime
        );
        return new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }
}
