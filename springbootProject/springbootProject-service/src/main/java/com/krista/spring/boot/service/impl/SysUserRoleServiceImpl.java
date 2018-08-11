package com.krista.spring.boot.service.impl;

import com.krista.spring.boot.dao.SysUserRoleMapper;
import com.krista.spring.boot.model.SysUserRole;
import com.krista.spring.boot.service.AbstractService;
import com.krista.spring.boot.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * SysUserRole
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
@Service
@Transactional
public class SysUserRoleServiceImpl extends AbstractService<SysUserRole> implements SysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    @Transactional
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        // 先删除用户与角色关系
        if(roleIdList == null || roleIdList.size() == 0){
            return ;
        }

        Condition condition = new Condition(SysUserRole.class);
        condition.createCriteria().andEqualTo("userId",userId);
        sysUserRoleMapper.deleteByCondition(condition);

        // 保存用户与角色关系
        List<SysUserRole> list = new ArrayList<>(roleIdList.size());
        for(Long roleId : roleIdList){
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            list.add(userRole);
        }
        sysUserRoleMapper.insertList(list);
    }

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return sysUserRoleMapper.queryRoleIdList(userId);
    }


    @Override
    public int deleteBatch(Long[] roleIds){
        return sysUserRoleMapper.deleteBatch(roleIds);
    }

}
