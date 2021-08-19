package com.sw.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.order.component.Auths;
import com.sw.order.entity.*;
import com.sw.order.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 权限的增删查改
 */
@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private Auths auths;

    /**
     * 添加权限，需要ROLE_AUTH_SOU权限
     * @param auth 要添加的权限信息
     * @return 添加成功
     */
    @Secured("ROLE_AUTH_SOU")
    @RequestMapping("/auth/sou")
    public Result authAdd(@RequestBody Auth auth) {
        authService.saveOrUpdate(auth);
        // 更新缓存
        auths.getAuthMap().put(auth.getAuthId(), auth);
        return Result.ok();
    }

    /**
     * 添加权限，需要ROLE_AUTH_DEL权限
     * @param authId 要删除的权限ID
     * @return 删除成功与否
     */
    @Secured("ROLE_AUTH_DEL")
    @RequestMapping("/auth/del")
    public Result goodDel(Integer authId) {
        if(authService.removeById(authId)) {
            // 删除缓存记录
            auths.getAuthMap().remove(authId);
            return Result.ok();
        } else {
            return Result.clientError();
        }
    }

    /**
     * 添加权限，需要ROLE_AUTH_SHOW权限
     * @param map curPage，pageSize，search，search_content
     * @return 删除成功与否
     */
    @Secured("ROLE_AUTH_SHOW")
    @RequestMapping("/authlist")
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
                if("authName".equals(search)) {
                    isName = true;
                } else if("authId".equals(search)) {
                    n = Long.parseLong(search_content);
                    isId = true;
                }
            }
        }
        QueryWrapper qe = new QueryWrapper();
        qe.like(isName, search, search_content);
        qe.eq(isId,"auth_id", n);
        IPage<Auth> iPage = authService.page(new Page<>(curPage, pageSize), qe);
        ResultPage<Auth> ePage = new ResultPage<>();
        ePage.setCurrent((long)curPage);
        ePage.setSize(pageSize);
        ePage.setTotal(iPage.getTotal());
        ePage.setEList(iPage.getRecords());
        return Result.ok().put("list", ePage);
    }
}
