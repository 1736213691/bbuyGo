package com.sw.orders.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

import javax.validation.constraints.DecimalMin;

@Setter
@Getter
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String username;
    private Integer roleId;
    private String password;
    @DecimalMin("0")
    private Integer wallet;
    @TableField(exist = false)
    private String avatar;
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.DATE)
    private String createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.DATE)
    private String updateDate;
}
