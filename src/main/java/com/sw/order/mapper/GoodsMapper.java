package com.sw.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sw.order.config.cache.MybatisRedisCache;
import com.sw.order.entity.Goods;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface GoodsMapper extends BaseMapper<Goods> {

}
