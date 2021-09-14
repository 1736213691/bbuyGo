package com.sw.orders.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.orders.entity.Role;
import com.sw.orders.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

@Service
public class RoleService implements IService<Role> {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public boolean saveBatch(Collection<Role> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Role> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Role> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Role entity) {
        if(entity.getRoleId() == null) {
            roleMapper.insert(entity);
        } else {
            roleMapper.updateById(entity);
        }
        return true;
    }

    @Override
    public Role getOne(Wrapper<Role> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Role> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Role> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<Role> getBaseMapper() {
        return roleMapper;
    }
}
