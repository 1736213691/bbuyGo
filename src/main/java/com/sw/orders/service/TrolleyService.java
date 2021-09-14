package com.sw.orders.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.orders.entity.Trolley;
import com.sw.orders.entity.vo.TrolleyVo;
import com.sw.orders.mapper.TrolleyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

@Service
public class TrolleyService implements IService<Trolley> {
    @Autowired
    private TrolleyMapper trolleyMapper;

    @Override
    public boolean saveBatch(Collection<Trolley> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Trolley> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Trolley> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Trolley entity) {
        if(entity.getTrolleyId() == null) {
            trolleyMapper.insert(entity);
        } else {
            trolleyMapper.updateById(entity);
        }
        return false;
    }

    @Override
    public Trolley getOne(Wrapper<Trolley> queryWrapper, boolean throwEx) {
        return trolleyMapper.selectOne(queryWrapper);
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Trolley> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Trolley> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<Trolley> getBaseMapper() {
        return trolleyMapper;
    }

    public IPage<TrolleyVo> pageList(Page<TrolleyVo> page, QueryWrapper qe) {
        return (IPage<TrolleyVo>) trolleyMapper.pageList(page, qe);
    }
}
