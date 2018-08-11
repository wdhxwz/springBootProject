package com.krista.spring.boot.dao;

import com.krista.spring.boot.MyMapper;
import com.krista.spring.boot.model.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper extends MyMapper<SysUserRole> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);


    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}