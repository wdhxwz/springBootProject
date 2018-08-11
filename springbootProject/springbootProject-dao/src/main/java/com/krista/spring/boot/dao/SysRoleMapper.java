package com.krista.spring.boot.dao;

import com.krista.spring.boot.MyMapper;
import com.krista.spring.boot.model.SysRole;

import java.util.List;

public interface SysRoleMapper extends MyMapper<SysRole> {

    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long createUserId);
}