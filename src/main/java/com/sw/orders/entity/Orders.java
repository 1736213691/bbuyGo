package com.sw.orders.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

@Setter
@Getter
public class Orders {
    @TableId(type = IdType.AUTO)
    private Integer orderId;
    private Integer goodId;
    private Integer userId;
    private Integer num;
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.DATETIMEOFFSET)
    private String createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.DATETIMEOFFSET)
    private String updateDate;
}
