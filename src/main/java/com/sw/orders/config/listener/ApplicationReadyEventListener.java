package com.sw.orders.config.listener;

import com.sw.orders.component.Auths;
import com.sw.orders.component.Roles;
import com.sw.orders.config.cache.MybatisRedisCache;
import com.sw.orders.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private Auths auths;
    @Autowired
    private Roles roles;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        auths.refresh();
        roles.refresh();
        MybatisRedisCache.setRedisTemplate((RedisTemplate<String, Object>) SpringUtil.getBean("redisTemplate"));
    }

}