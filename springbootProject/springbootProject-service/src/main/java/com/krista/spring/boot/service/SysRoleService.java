package com.krista.spring.boot.service;

import com.krista.spring.boot.model.SysRole;

import java.util.List;


/**
 * SysRole
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
public interface SysRoleService extends Service<SysRole> {

    void saveRole(SysRole role);

    void updateRole(SysRole role);

    void deleteBatch(Long[] roleIds);

    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long createUserId);
}
