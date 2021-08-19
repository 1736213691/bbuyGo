package com.sw.order.config.listener;

import com.sw.order.component.Auths;
import com.sw.order.component.Roles;
import com.sw.order.config.cache.MybatisRedisCache;
import com.sw.order.util.SpringUtil;
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