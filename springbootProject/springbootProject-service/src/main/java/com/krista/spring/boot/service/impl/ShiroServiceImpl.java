package com.krista.spring.boot.service.impl;

import com.krista.spring.boot.dao.SysMenuMapper;
import com.krista.spring.boot.dao.SysUserMapper;
import com.krista.spring.boot.dao.SysUserTokenMapper;
import com.krista.spring.boot.dao.redis.RedisUtils;
import com.krista.spring.boot.model.SysMenu;
import com.krista.spring.boot.model.SysUser;
import com.krista.spring.boot.model.SysUserToken;
import com.krista.spring.boot.service.ShiroService;
import com.krista.spring.boot.service.SysUserService;
import com.krista.spring.boot.utils.RedisKeys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserTokenMapper sysUserTokenMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (sysUserService.isAdmin(userId)) {
            List<SysMenu> menuList = sysMenuMapper.selectAll();
            permsList = new ArrayList<>(menuList.size());
            for (SysMenu menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserMapper.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserToken queryByToken(String token) {
        SysUserToken sysUserToken = redisUtils.get(RedisKeys.getTokenKey(token),SysUserToken.class);

        return sysUserToken;
        // return sysUserTokenMapper.queryByToken(token);
    }

    @Override
    public SysUser queryUser(Long userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }
}
