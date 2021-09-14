package com.sw.orders.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Goods implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer goodId;
    @NotNull
    @NotBlank(message = "商品名不为空")
    private String name;
    @NotNull
    @DecimalMin("0")
    private Integer num;
    private Integer limitN;
    @NotNull
    @DecimalMin("0")
    private Integer price;
//    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String buyStart;
//    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String buyEnd;
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateDate;
}
