package com.sw.orders.component;

import com.sw.orders.entity.Role;
import com.sw.orders.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 角色信息缓存类
 */
@Component
public class Roles {
    @Autowired
    private RoleMapper roleMapper;
    private Map<Integer, Role> roleMap;

    /**
     * 获取所有的角色
     * @return
     */
    public void refresh() {
        roleMap = new HashMap<>();
        for(Role r : roleMapper.selectList(null)) {
            roleMap.put(r.getRoleId(), r);
        }
    }

    public Map<Integer, Role> getRoleMap() {
        return roleMap;
    }
}
