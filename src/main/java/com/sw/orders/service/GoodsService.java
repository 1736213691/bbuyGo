package com.sw.orders.service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.orders.entity.Goods;
import com.sw.orders.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

@Service
public class GoodsService implements IService<Goods> {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public boolean saveBatch(Collection<Goods> entityList, int batchSize) {

        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Goods> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Goods> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Goods entity) {
        if(entity.getGoodId() == null) {
            goodsMapper.insert(entity);
        } else {
            goodsMapper.updateById(entity);
        }
        return true;
    }

    @Override
    public Goods getOne(Wrapper<Goods> queryWrapper, boolean throwEx) {
        return goodsMapper.selectOne(queryWrapper);
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Goods> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Goods> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<Goods> getBaseMapper() {
        return goodsMapper;
    }

    @Override
    public <E extends IPage<Goods>> E page(E page, Wrapper<Goods> queryWrapper) {
        return goodsMapper.selectPage(page,queryWrapper);
    }
}
