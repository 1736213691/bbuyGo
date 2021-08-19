package com.sw.order.component;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sw.order.entity.Auth;
import com.sw.order.mapper.AuthMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
