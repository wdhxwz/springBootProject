package com.krista.spring.boot.service.impl;

import com.krista.spring.boot.dao.SysRoleMapper;
import com.krista.spring.boot.dto.exception.ServiceException;
import com.krista.spring.boot.dto.result.ResultCode;
import com.krista.spring.boot.model.SysRole;
import com.krista.spring.boot.service.*;
import com.krista.spring.boot.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * SysRole
 *
 * @author jeikerxiao
 * @version 2018/06/27
 */
@Service
@Transactional
public class SysRoleServiceImpl extends AbstractService<SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRole role) {
        role.setCreateTime(new Date());
        sysRoleMapper.insert(role);

        // 检查权限是否越权
        checkPrems(role);

        // 保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRole role) {
        sysRoleMapper.updateByPrimaryKeySelective(role);
        //检查权限是否越权
        checkPrems(role);
        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        for (Long roleId : roleIds) {
            SysRole sysRole = new SysRole();
            sysRole.setRoleId(roleId);
            sysRoleMapper.delete(sysRole);
        }
        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);
        //删除角色与用户关联
        sysUserRoleService.deleteBatch(roleIds);
    }

    @Override
    public List<Long> queryRoleIdList(Long createUserId) {
        return sysRoleMapper.queryRoleIdList(createUserId);
    }

    /**
     * 检查权限是否越权
     */
    private void checkPrems(SysRole role) {
        //如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
        if (role.getCreateUserId() == Constant.SUPER_ADMIN) {
            return;
        }
        //查询用户所拥有的菜单列表
        List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());
        //判断是否越权
        if (!menuIdList.containsAll(role.getMenuIdList())) {
            throw new ServiceException(ResultCode.USER_NEW_ROLE_ERROR);

        }
    }
}
