package com.krista.spring.boot.dao;

import com.krista.spring.boot.MyMapper;
import com.krista.spring.boot.model.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuMapper extends MyMapper<SysRoleMenu> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}