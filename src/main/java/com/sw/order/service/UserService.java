package com.sw.order.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.order.entity.User;
import com.sw.order.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * 
 */
@Service
public class UserService implements IService<User> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean saveBatch(Collection<User> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<User> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<User> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(User entity) {
        if(entity.getUserId() == null) {
            entity.setPassword(DigestUtils.md5DigestAsHex(entity.getPassword().getBytes()));
            userMapper.insert(entity);
        } else {
            userMapper.updateById(entity);
        }
        return true;
    }

    @Override
    public User getOne(Wrapper<User> queryWrapper, boolean throwEx) {
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Map<String, Object> getMap(Wrapper<User> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<User> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<User> getBaseMapper() {
        return userMapper;
    }

}
