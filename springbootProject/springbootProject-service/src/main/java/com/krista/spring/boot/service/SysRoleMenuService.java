package com.krista.spring.boot.service;

import com.krista.spring.boot.model.SysRoleMenu;

import java.util.List;


/**
 * SysRoleMenu
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
public interface SysRoleMenuService extends Service<SysRoleMenu> {

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}
