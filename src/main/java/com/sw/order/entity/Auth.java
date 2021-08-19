package com.sw.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Auth {
    @TableId(type = IdType.AUTO)
    private Integer authId;
    private String authName;
    private String authZhName;
}