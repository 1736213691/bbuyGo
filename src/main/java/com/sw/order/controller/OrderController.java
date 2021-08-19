package com.sw.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.order.config.handler.GlobalExceptionHandler;
import com.sw.order.config.handler.MybatisFillHandler;
import com.sw.order.entity.*;
import com.sw.order.service.GoodsService;
import com.sw.order.service.OrderService;
import com.sw.order.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单增删改查
 */
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;


    /**
     * 核心接口
     * 购买商品指定ID的商品，转为订单
     * 进行商品库存，用户余额，限购数量，抢购时间判断
     * @return 删除成功与否
     */
    @Transactional(rollbackFor=Exception.class)
    @Secured("ROLE_GOODS_BUY")
    @GetMapping("/buy")
    public synchronized Result orderInfo(Integer id, HttpSession session) throws ParseException {
        Integer n = 1;
        // 获取要购买的货物信息
        Goods goods = goodsService.getById(id);
        if(goods == null) return Result.clientError("商品不存在");
        if(goods.getNum() - n < 0) {
            return Result.clientError("缺货");
        }
        // 当前购买的用户
        User user = (User)session.getAttribute("user");
        if(user.getWallet() < goods.getPrice() * n) {
            return Result.clientError("余额不足");
        }
        QueryWrapper qe = new QueryWrapper<>();
        qe.eq("user_id", user.getUserId());
        qe.eq("good_id", id);
        Integer od = orderService.count(qe);
        if(od >= goods.getLimitN()) return Result.clientError("购买数量上限");

        if(!"未设置".equals(goods.getBuyStart())) {
            Date start = MybatisFillHandler.df.parse(goods.getBuyStart());
            Date end = MybatisFillHandler.df.parse(goods.getBuyEnd());
            long time = new Date().getTime();
            if (start.getTime() > time && time >= end.getTime()) {
                return Result.clientError("不在抢购时间");
            }
        }
        Orders orders = new Orders();
        orders.setGoodId(id);
        orders.setNum(n);
        // 并发问题
        orders.setUserId(user.getUserId());

        orderService.save(orders);
        goods.setNum(goods.getNum()-n);
        user.setWallet(user.getWallet() - goods.getPrice() * n);
        userService.updateById(user);
        goodsService.updateById(goods);
        return Result.ok("购买成功");
    }

//    @Secured("ROLE_ORDER_SHOW")
    @RequestMapping({"/order/sou", "/order/del"})
    public Result a() {
        return Result.clientError(9000, "暂不支持订单直接修改和添加，请联系客服或者改订单创建的用户");
    }

    /**
     * 获取订单列表，需要ROLE_AUTH_SHOW权限
     * @param map curPage，pageSize，search，search_content
     * @return 删除成功与否
     */
    @Secured("ROLE_ORDER_SHOW")
    @RequestMapping("/orderlist")
    public Result orderlist(@RequestBody Map<String, String> map) {
        Long curPage = Long.parseLong(map.get("curPage"));
        Long pageSize = Long.parseLong(map.get("pageSize"));
        if(curPage < 1 && (pageSize >= 10 && pageSize <= 200)) {
            return Result.clientError("页码错误");
        }
        String search = map.get("search");
        String search_content = map.get("search_content");
        Long n = null;
        boolean isOId = false;
        boolean isGId = false;
        boolean isUId = false;
        if(search != null && search_content != null) {
            if(search_content.length() > 0) {
                switch (search) {
                    case "orderId" : isOId = true; break;
                    case "goodId" : isGId = true; break;
                    case "userId" : isUId = true; break;
                }
            }
        }
        QueryWrapper qe = new QueryWrapper();
        qe.eq(isUId,"user_id", search_content);
        qe.eq(isOId,"order_id", search_content);
        qe.eq(isGId,"good_id", search_content);
        IPage<Orders> iPage = orderService.page(new Page<>(curPage, pageSize), qe);
        ResultPage<Orders> ePage = new ResultPage<>();
        ePage.setCurrent((long)curPage);
        ePage.setSize(pageSize);
        ePage.setTotal(iPage.getTotal());
        ePage.setEList(iPage.getRecords());
        return Result.ok().put("list", ePage);
    }


    /**
     * 获取7-30天的订单数据
     * @return 删除成功与否
     */
    @Secured("ROLE_ORDER_SHOW")
    @RequestMapping("/order/statistics")
    public Result order(Integer day) {
        if(day == null) return Result.clientError();
        day = day > 30 ? 30 : day;
        day = day < 7 ? 7 : day;
        QueryWrapper qe = new QueryWrapper<>();
        qe.le("datediff(now(),date_format(`update_date`, '%Y-%m-%d %H:%i:%s'))", day);
        qe.orderByDesc("update_date");
        Result result = Result.ok();
        result.put("data", orderService.list(qe));
        return result;
    }
}
