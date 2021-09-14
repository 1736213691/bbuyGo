package com.sw.orders.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.orders.component.Auths;
import com.sw.orders.component.Roles;
import com.sw.orders.entity.*;
import com.sw.orders.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private Roles roles;
    @Autowired
    private Auths auths;

    /**
     * 新增或更新角色信息
     */
    @Secured("ROLE_ROLE_SOU")
    @RequestMapping("/role/sou")
    public Result roleAdd(@RequestBody Role role) {
        if(role == null) {
            return Result.clientError();
        }
        for(String a : role.getAuthList().split(",")) {
            if(auths.getAuthMap().get(Integer.parseInt(a)) == null) {
                return Result.clientError("权限不存在");
            }
        }
        roleService.saveOrUpdate(role);
        roles.getRoleMap().put(role.getRoleId(), role);
        return Result.ok(2000, "更新成功");
    }

    /**
     * 删除角色
     * @return 删除成功与否
     */
    @Secured("ROLE_ROLE_DEL")
    @RequestMapping("/role/del/{roleId}")
    public Result roleDel(@PathVariable Integer roleId) {
        if(roleService.removeById(roleId)) {
            // 删除缓存记录
            roles.getRoleMap().remove(roleId);
            return Result.ok(2000, "删除成功");
        } else {
            return Result.clientError();
        }
    }

    /**
     * 获取角色列表
     * @param map curPage，pageSize，search，search_content
     */
    @Secured("ROLE_ROLE_SHOW")
    @RequestMapping("/rolelist")
    public Result roles(@RequestBody Map<String, String> map) {
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
                if("roleName".equals(search)) {
                    isName = true;
                } else if("roleId".equals(search)) {
                    n = Long.parseLong(search_content);
                    isId = true;
                }
            }
        }
        QueryWrapper qe = new QueryWrapper();
        qe.like(isName, search, search_content);
        qe.like(isId,"role_id", n);
        IPage<Role> iPage = roleService.page(new Page<>(curPage, pageSize), qe);
        ResultPage<Role> ePage = new ResultPage<>();
        ePage.setCurrent((long)curPage);
        ePage.setSize(pageSize);
        ePage.setTotal(iPage.getTotal());
        ePage.setEList(iPage.getRecords());
        return Result.ok().put("list", ePage);
    }
}
