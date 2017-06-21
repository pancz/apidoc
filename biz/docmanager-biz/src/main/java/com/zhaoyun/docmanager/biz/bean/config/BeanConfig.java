package com.zhaoyun.docmanager.biz.bean.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/8/26 15:19.
 * 未来不确定才更值得前行
 *
 * @author chengyibin
 */
@Configuration
public class BeanConfig {
    @Bean
    public ThreadPoolExecutor auxThreadPool() {
        return new ThreadPoolExecutor(16, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(64));
    }
}
