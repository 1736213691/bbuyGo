package com.sw.orders.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sw.orders.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
