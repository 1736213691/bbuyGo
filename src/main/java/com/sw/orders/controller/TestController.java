package com.sw.orders.controller;

import com.sw.orders.component.mq.MsgQueue;
import com.sw.orders.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private MsgQueue<String> msgQueue;

    private int count = 1;

    @RequestMapping("/MQTest")
    public Result test(@RequestBody String msg) {
        msgQueue.product(msg + count++ + ",");
        return Result.ok();
    }
}
