package com.sw.orders.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
//使用注解注入配置，必须要添加，这样application.properties中的配置才能在redis中生效，添加@Configuration之后，spring 会自动扫描注入
public class RedisConfig {

    @Bean //声明一个Bean 这样其他地方可以通过@Autowired获取到生成的 RedisTemplate 对象
    public RedisTemplate rt(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);// 配置连接工厂　　　　　　
        // 设置键值默认序列化方式　　　　
        // RedisSerializer stringSerializer = new StringRedisSerializer();
        // RedisTemplate有自己的默认序列化的方式，不过使用默认方式，会在redis客户端查看的时候出现乱码，不便与使用　　　　
        // template.setDefaultSerializer(stringSerializer);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}