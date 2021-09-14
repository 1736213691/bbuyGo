package com.sw.orders.config.handler;

import com.sw.orders.entity.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.UnexpectedTypeException;
import java.text.ParseException;

/**
 * 全局异常处理
 */
@ControllerAdvice
//@Slf4j
public class GlobalExceptionHandler {

    public static class UniversalException extends Exception {
        public UniversalException() {}
        public UniversalException(String msg) {super(msg);}
    }

    public static class DateRangeException extends Exception {
        public DateRangeException() {}
        public DateRangeException(String msg) {super(msg);}
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result defaultErrorHandler(Exception e) {
        if(e instanceof UniversalException) {
            return Result.clientError(e.getMessage());
        } else if (e instanceof  NumberFormatException) {
            return Result.clientError(500, "数字格式不正确");
        } else if(e instanceof ParseException) {
            return Result.clientError("日期格式错误");
        } else if(e instanceof DateRangeException) {
            return Result.clientError("日期范围错误, 请设置一个大于等于30秒的日期");
        } else if(e instanceof UnexpectedTypeException) {
            return Result.clientError("输入不合法，数据不能为空，或者范围错误");
        } else if(e instanceof AccessDeniedException) {
            return Result.clientError(607,"权限不足");
        }
        e.printStackTrace();
        return Result.clientError("未知错误");
    }
}
