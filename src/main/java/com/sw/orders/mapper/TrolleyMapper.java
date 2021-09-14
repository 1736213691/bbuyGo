package com.sw.orders.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.orders.entity.Trolley;
import com.sw.orders.entity.vo.TrolleyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TrolleyMapper extends BaseMapper<Trolley> {
    /**
     * @param page 分页对象
     * @param wrapper 传参
     * @return
     */
    @Select("select t.trolley_id,g.name,g.num singleNum,g.price,t.num buyNum,t.create_date from trolley t join goods g on t.good_id = g.good_id ${ew.customSqlSegment}")
    IPage<TrolleyVo> pageList(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
