package com.sw.orders.component;

import com.sw.orders.entity.Auth;
import com.sw.orders.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户权限缓存类
 */
@Component
public class Auths {
    @Autowired
    private AuthMapper authMapper;
    // 因为根据权限 id 来找权限，所以用List
    private Map<Integer, Auth> authMap;

    /**
     * 从数据库中获取所有的权限
     * @return
     */
    public void refresh() {
        authMap = new HashMap<>();
        for(Auth a : authMapper.selectList(null)) {
            authMap.put(a.getAuthId(), a);
        }
    }

    public Map<Integer, Auth> getAuthMap() {
        return authMap;
    }
}
