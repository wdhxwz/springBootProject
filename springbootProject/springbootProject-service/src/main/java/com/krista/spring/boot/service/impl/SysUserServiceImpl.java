package com.krista.spring.boot.service.impl;

import com.krista.spring.boot.dao.SysUserMapper;
import com.krista.spring.boot.dto.exception.ServiceException;
import com.krista.spring.boot.dto.result.ResultCode;
import com.krista.spring.boot.model.SysUser;
import com.krista.spring.boot.service.AbstractService;
import com.krista.spring.boot.service.SysRoleService;
import com.krista.spring.boot.service.SysUserRoleService;
import com.krista.spring.boot.service.SysUserService;
import com.krista.spring.boot.utils.Constant;
import com.krista.spring.boot.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * SysUser
 *
 * @author jeikerxiao
 * @version 2018/06/27
 */
@Service
@Transactional
@Slf4j
public class SysUserServiceImpl extends AbstractService<SysUser> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleService sysRoleService;

    @Override
    @Transactional
    public void add(SysUser user) {
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash(MD5Utils.getMD5Code(user.getPassword()), salt).toHex());
        user.setSalt(salt);
        user.setCreateTime(new Date());
        sysUserMapper.insertSelective(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void updateUser(SysUser user) {
        sysUserMapper.updateByPrimaryKeySelective(user);

        // 更新用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void deleteByIds(Long[] userIds) {
        sysUserMapper.deleteByIds(
                ArrayUtils.toString(userIds)
                        .replace("{","")
                        .replace("}",""));
    }

    @Override
    public List<String> queryAllPerms(Long userId) {
        return sysUserMapper.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return sysUserMapper.queryAllMenuId(userId);
    }

    @Override
    public SysUser queryByUserName(String userName) {
        SysUser user = new SysUser();
        user.setUsername(userName);
        List<SysUser> userList = sysUserMapper.select(user);
        if(userList == null || userList.isEmpty()){
            return null;
        }

        return userList.get(0);
    }

    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUser user = new SysUser();
        user.setPassword(newPassword);

        Condition condition = new Condition(SysUser.class);
        condition.createCriteria()
                .andCondition("user_id = ", userId)
                .andCondition("password = ", password);
        int result = sysUserMapper.updateByConditionSelective(user, condition);
        if (result <= 0) {
            log.error("updatePassword failure result: {}", result);
            return false;
        }
        return true;
    }

    @Override
    public SysUser getUserWithRole(Long userId) {
        SysUser sysUser = findById(userId);

        // 设置角色信息
        sysUser.setRoleIdList(sysUserRoleService.queryRoleIdList(userId));

        return sysUser;
    }

    @Override
    public boolean isAdmin(Long userId) {
        List<Long> roleIds = sysUserRoleService.queryRoleIdList(userId);

        return roleIds.contains(Constant.SUPER_ADMIN);
    }

    /**
     * 检查角色是否越权
     */
    private void checkRole(SysUser user){
        if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
            return;
        }
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if(user.getCreateUserId() == Constant.SUPER_ADMIN){
            return ;
        }
        //查询用户创建的角色列表
        List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());
        //判断是否越权
        if(!roleIdList.containsAll(user.getRoleIdList())){
            throw new ServiceException(ResultCode.USER_ROLE_ERROR);
        }
    }
}
