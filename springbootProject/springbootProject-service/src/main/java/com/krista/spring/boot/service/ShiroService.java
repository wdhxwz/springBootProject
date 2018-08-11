package com.krista.spring.boot.service;

import com.krista.spring.boot.model.SysUser;
import com.krista.spring.boot.model.SysUserToken;

import java.util.Set;

/**
 * Shiro 接口
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    /**
     * 根据token，查询用户
     */
    SysUserToken queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUser queryUser(Long userId);
}
