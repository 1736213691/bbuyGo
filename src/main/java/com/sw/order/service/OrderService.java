package com.sw.order.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.order.entity.Orders;
import com.sw.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

@Service
public class OrderService implements IService<Orders> {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public boolean saveBatch(Collection<Orders> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Orders> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Orders> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Orders entity) {
        return false;
    }

    @Override
    public Orders getOne(Wrapper<Orders> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Orders> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Orders> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<Orders> getBaseMapper() {
        return orderMapper;
    }
}
