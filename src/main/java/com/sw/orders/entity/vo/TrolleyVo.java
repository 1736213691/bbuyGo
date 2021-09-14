package com.sw.orders.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class TrolleyVo {
    private Integer trolleyId;
    private String name;
    private Integer price;
    private Integer buyNum;
    private Integer singleNum;
    @TableField(exist = false)
    private Integer total;
    private String createDate;
}
