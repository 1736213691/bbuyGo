package com.sw.orders;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.orders.component.Auths;
import com.sw.orders.component.Roles;
import com.sw.orders.component.mq.MsgQueue;
import com.sw.orders.entity.User;
import com.sw.orders.mapper.TrolleyMapper;
import com.sw.orders.service.GoodsService;
import com.sw.orders.service.OrderService;
import com.sw.orders.service.TrolleyService;
import com.sw.orders.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderApplicationTests {
    @Autowired
    TrolleyService trolleyService;
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
    @Autowired
    TrolleyMapper orderMapper;
    @Value("${file.path}/userfile")
    private String path;
//    @Test
//    public void trolley() {
//        QueryWrapper qe = new QueryWrapper();
//        qe.eq("trolley_id", 1);
//        Trolley one = trolleyService.getOne(qe);
//        System.out.println(one);
//    }


    @Test
    public void t10() throws InterruptedException {
//        Integer num = 1;
//        Page page = (Page) redisCacheUtil.get("goods", num);
//        if(page == null) {
//            page = goodsService.page(new Page<>(num, 2));
//            redisCacheUtil.add("goods", page);
//        }
//
//        Page page1 = (Page) redisCacheUtil.get("goods", num);
//        System.out.println(page1);
//        redisCacheUtil.clear("goods");
//        Page page2 = (Page) redisCacheUtil.get("goods", num);
//        System.out.println(page2);
//a();
        MsgQueue<User> mq = new MsgQueue<User>() {
            @Override
            public void consumer() {
                User user = poll();
                System.out.println(user.getWallet());
                userService.save(user);
            }
        };
        new Thread(() -> a(mq)).start();
//        new Thread(() -> a(mq)).start();
//        new Thread(() -> a(mq)).start();
//        new Thread(() -> a(mq)).start();


        Thread.sleep(8000);
//        System.out.println();
    }

    public void a(MsgQueue<User> mq) {
        for(int i = 0; i < 800; i++){
            QueryWrapper qw = new QueryWrapper();
            qw.eq("user_id", 547);
            User user = userService.getOne(qw);
            user.setWallet(user.getWallet() + 1);
            mq.product(user);
//            System.out.println(Thread.currentThread().getName() + ": " + i + "-" + userService.save(user));
        }
    }
//    @Test
//    @Ignore
//    public void authAndRole() {
//        auths.refresh();
//        roles.refresh();
//        QueryWrapper qe = new QueryWrapper();
//        qe.eq("user_id" , 6);
//        User user = userService.getOne(qe);
//        for(String auth : roles.getRoleMap().get(user.getRoleId()).getAuthList().split(",")) {
//            // 把用户权限id映射为权限名
//            Assertions.assertEquals(auths.getAuthMap().get(Integer.parseInt(auth)).getAuthName().substring(0,5), "ROLE_");
//        }
//    }

//    @Test
//    public void test1() {
//        Page<Goods> page = new Page<>(1,10);
//        System.out.println(goodsService.page(page));
//    }
//
//    @Test
//    public void test2() {
//        Goods goods = new Goods();
//        goods.setName("test_goods");
//        goodsService.save(goods);
//        Assert.assertNotNull(goods.getGoodId());
//    }
//
//    @Test
//    public void test3() {
//        QueryWrapper qe = new QueryWrapper();
//        qe.eq("name", "test_goods");
//        Assert.assertTrue(goodsService.remove(qe));
//    }
}
