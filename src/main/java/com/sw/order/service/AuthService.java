package com.sw.order.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.order.entity.Auth;
import com.sw.order.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

@Service
public class AuthService implements IService<Auth> {
    @Autowired
    private AuthMapper authMapper;

    @Override
    public boolean saveBatch(Collection<Auth> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Auth> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Auth> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Auth entity) {
        if(entity.getAuthId() == null) {
            authMapper.insert(entity);
        } else authMapper.updateById(entity);
        return false;
    }

    @Override
    public Auth getOne(Wrapper<Auth> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Auth> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Auth> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<Auth> getBaseMapper() {
        return authMapper;
    }
}
