package com.sw.order.entity;

import lombok.Data;

import java.util.List;

/**
 * 分页查询封装
 */
@Data
public class ResultPage<E> {
    private Long current;
    private Long size;
    private Long total;
    private List<E> eList;
}
