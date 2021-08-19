package com.sw.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.order.component.Auths;
import com.sw.order.component.Roles;
import com.sw.order.entity.*;
import com.sw.order.service.GoodsService;
import com.sw.order.service.OrderService;
import com.sw.order.service.UserService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderApplicationTests {
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserService userService;
    @Autowired
    Auths auths;
    @Autowired
    Roles roles;

    @Test
    @Ignore
    public void authAndRole() {
        auths.refresh();
        roles.refresh();
        QueryWrapper qe = new QueryWrapper();
        qe.eq("user_id" , 6);
        User user = userService.getOne(qe);
        for(String auth : roles.getRoleMap().get(user.getRoleId()).getAuthList().split(",")) {
            // 把用户权限id映射为权限名
            Assertions.assertEquals(auths.getAuthMap().get(Integer.parseInt(auth)).getAuthName().substring(0,5), "ROLE_");
        }
    }

    @Test
    public void user() {
        User user = new User();
        user.setUsername("userTest");
        user.setPassword("userTest");
        userService.save(user);
        Integer id = user.getUserId();
        QueryWrapper qe = new QueryWrapper();
        qe.eq("user_id", id);
        User user2 = userService.getOne(qe);
        Assert.assertEquals(user.getUsername(), "userTest");

        QueryWrapper qe2 = new QueryWrapper();
        qe.eq("user_id", id);
        userService.remove(qe2);
//        qe.eq("user_id", user.getUserId());

        QueryWrapper qe3 = new QueryWrapper();
        qe.eq("user_id", id);
        Assert.assertNull(userService.getOne(qe3));
    }
}
