package com.sw.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.order.config.handler.GlobalExceptionHandler;
import com.sw.order.config.handler.MybatisFillHandler;
import com.sw.order.entity.Goods;
import com.sw.order.entity.Result;
import com.sw.order.entity.ResultPage;
import com.sw.order.entity.User;
import com.sw.order.service.GoodsService;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品增删改查

 */
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    private String goodsFilePath = new File("").getCanonicalPath() + "\\src\\main\\resources" + File.separator + "static" + File.separator + "goodsImg";

    public GoodsController() throws IOException {
    }

    /**
     * 获取指定商品名，的商品图片
     */
    @GetMapping("/goods/downloadImg/{goodsName}")
    public ResponseEntity<Resource> download(@PathVariable("goodsName") String goodsName) throws IOException {
        File file1 = new File(goodsFilePath,  goodsName);
        if(file1.exists() == false) {
            file1 = new File(goodsFilePath, "default");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tag.jpg\"")
                .body(new UrlResource(new File(file1, "tag.jpg").toURI()));
    }

    /**
     * 上传商品图片
     */
    @PostMapping("/goods/uploadImg/{goodsName}")
    public Result uploadImg(MultipartFile file, @PathVariable String goodsName) throws IOException {
        if(file == null) {
            return Result.clientError("请上传正确的文件");
        }
        File file1 = new File(goodsFilePath,  goodsName);
        if(file1.exists() == false)
            file1.mkdirs();
        file.transferTo(new File(file1, "tag.jpg"));
        return Result.ok();
    }

    /**
     * /good/detail 接口 get请求传入id
     * @param id
     * @return 返回查询结果
     */
    @Secured("ROLE_GOODS_SHOW")
    @RequestMapping("/good/detail")
    public Result goodsOne(Integer id) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("good_id", id);
        return Result.ok().put("good", goodsService.getOne(qw));
    }

    /**
     * /good/sou 接口  post请求，当 goodid 等于 null 时 使用插入操作，否则更新操作
     * @param goods Goods类
     * @return 请求成功与否
     * @throws ParseException 传入的日期格式错误
     * @throws GlobalExceptionHandler.DateRangeException 日期范围错误
     */
    @Secured("ROLE_GOODS_SOU")
    @RequestMapping("/good/sou")
    public Result goodAdd(@Validated @RequestBody Goods goods) throws ParseException, GlobalExceptionHandler.DateRangeException {
        if(goods.getBuyStart() != null && goods.getBuyEnd() != null && goods.getBuyStart().length() > 5 && goods.getBuyEnd().length() > 5) {
            Date start = MybatisFillHandler.df.parse(goods.getBuyStart());
            Date end = MybatisFillHandler.df.parse(goods.getBuyEnd());
            if(end.getTime() - start.getTime() < 30000) {
                throw new GlobalExceptionHandler.DateRangeException();
            }
        }
        goodsService.saveOrUpdate(goods);
        return Result.ok();
    }

    /**
     * /good/del
     * @param goodId get方式删除
     * @return 删除结果
     */
    @Secured("ROLE_GOODS_DEL")
    @RequestMapping("/good/del")
    public Result goodDel(Integer goodId) {
        return goodsService.removeById(goodId) ? Result.ok() : Result.clientError();
    }

    /**
     * /goodlist
     * @param map 查询条件与分页参数
     * @return 商品列表
     */
    @Secured("ROLE_GOODS_SHOW")
//    @PreAuthorize("hasAuthority('ROLE_GOODS_SHOW')")
    @RequestMapping("/goodlist")
    public Result goods(@RequestBody Map<String, String> map) {
        Long curPage = Long.parseLong(map.get("curPage"));
        Long pageSize = Long.parseLong(map.get("pageSize"));
        if(curPage < 1 && (pageSize >= 10 && pageSize <= 200)) {
            return Result.clientError("页码错误");
        }
        String search = map.get("search");
        String search_content = map.get("search_content");
        Long n = null;
        boolean isName = false;
        boolean isId = false;
        if(search != null && search_content != null) {
            if(search_content.length() > 0) {
                if(search.equals("name")) {
                    isName = true;
                } else if(search.equals("goodId")) {
                    n = Long.parseLong(search_content);
                    isId = true;
                }
            }
        }
        QueryWrapper qe = new QueryWrapper();
        qe.like(isName, search, search_content);
        qe.eq(isId,"good_id", n);
        IPage<Goods> iPage = goodsService.page(new Page<>(curPage, pageSize), qe);
        ResultPage<Goods> ePage = new ResultPage<>();
        ePage.setCurrent((long)curPage);
        ePage.setSize(pageSize);
        ePage.setTotal(iPage.getTotal());
        ePage.setEList(iPage.getRecords());
        return Result.ok().put("list", ePage);
    }
}
