package com.sw.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sw.order.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
