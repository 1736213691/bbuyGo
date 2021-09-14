package com.sw.orders.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装响应结果
 */
@Data
public class Result {
    // 状态码
    private Integer code;
    // 描述
    private String msg;
    // 对象
    private Map<String, Object> data;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result() {

    }

    public static Result ok() {return new Result(200, "success");}
    public static Result ok(String msg) {return new Result(200, msg);}
    public static Result ok(Integer code, String msg) {return new Result(code, msg);}

    public static Result serverError() {return new Result(500, "error");}
    public static Result clientError() {return new Result(400, "操作不合法");}
    public static Result serverError(String msg) {return new Result(500, msg);}
    public static Result clientError(String msg) {return new Result(400, msg);}
    public static Result clientError(int i, String s) {return new Result(i, s);}

    public Result put(String key, Object val) {
        if(data == null) {
            data = new HashMap<>(16);
        }
        this.data.put(key, val);
        return this;
    }

    public String toJson() throws JsonProcessingException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("{\"code\":");
//        sb.append(code);
//        sb.append(",\"msg\":\"");
//        sb.append(msg);
//        sb.append("\",\"data\":null}");
//        return sb.toString();
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);

    }
}
