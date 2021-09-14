package com.sw.orders.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.orders.entity.*;
import com.sw.orders.entity.vo.TrolleyVo;
import com.sw.orders.service.TrolleyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
public class TrolleyController {
    @Autowired
    private TrolleyService trolleyService;

    @RequestMapping("/trolleylist")
    public Result trolleyList(@RequestBody Map<String, String> map, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Long curPage = Long.parseLong(map.get("curPage"));
        Long pageSize = Long.parseLong(map.get("pageSize"));
        if (curPage < 1 && (pageSize >= 10 && pageSize <= 200)) {
            return Result.clientError("页码错误");
        }
        String search = map.get("search");
        String search_content = map.get("search_content");
        Long n = null;

        QueryWrapper qe = new QueryWrapper();
        qe.like("name", search_content);
        qe.eq("user_id", user.getUserId());
        IPage<TrolleyVo> iPage = trolleyService.pageList(new Page<>(curPage, pageSize), qe);
        ResultPage<TrolleyVo> ePage = new ResultPage<>();
        ePage.setCurrent((long) curPage);
        ePage.setSize(pageSize);
        ePage.setTotal(iPage.getTotal());
        ePage.setEList(iPage.getRecords());
        return Result.ok().put("list", ePage);
    }

    @RequestMapping("/addtrolley")
    public Result addTrolley(@RequestBody List<Goods> goodsList, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (goodsList == null) {
            return Result.clientError("未选择商品");
        }
        for (Goods goods : goodsList) {
            Trolley trolley = new Trolley();
            trolley.setGoodId(goods.getGoodId());
            trolley.setNum(1);
            trolley.setUserId(user.getUserId());
            trolleyService.saveOrUpdate(trolley);
        }
        return Result.ok(2000, "添加成功");
    }

    @RequestMapping("/trolley/sou")
    public Result addTrolley(@RequestBody Trolley trolley, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (trolley == null) {
            return Result.clientError("未选择商品");
        }
        trolley.setUserId(user.getUserId());
        trolleyService.saveOrUpdate(trolley);

        return Result.ok(2000, "添加成功");
    }

    @RequestMapping("/trolley/del/{trolleyId}")
    public Result delTrolley(@PathVariable Integer trolleyId, HttpSession session) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("trolley_id", trolleyId);
        qw.eq("user_id", ((User) session.getAttribute("user")).getUserId());
        trolleyService.remove(qw);
        return Result.ok(2000, "删除成功");
    }

    @RequestMapping("/trolley/del")
    public Result delTrolley(@RequestBody List<TrolleyVo> TrolleyVoList, HttpSession session) {
        Integer userId = ((User) session.getAttribute("user")).getUserId();
        for (TrolleyVo vo : TrolleyVoList) {
            QueryWrapper qw = new QueryWrapper();
            qw.eq("user_id", userId);
            qw.eq("trolley_id", vo.getTrolleyId());
            trolleyService.remove(qw);
        }
        return Result.ok(2000, "批量删除成功");
    }
}
