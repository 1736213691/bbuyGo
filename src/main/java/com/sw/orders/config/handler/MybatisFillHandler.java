package com.sw.orders.config.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * mybatis 字段自动填充
 * 对创建时间和更新时间进行自动插入
 */
@Component
public class MybatisFillHandler implements MetaObjectHandler {

    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void insertFill(MetaObject metaObject) {
        String date = df.format(new Date());
        setFieldValByName("updateDate", date, metaObject);
        setFieldValByName("createDate", date, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String date = df.format(new Date());
        setFieldValByName("updateDate", date, metaObject);
    }
}