package com.krista.spring.boot.base.web.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.krista.spring.boot.base.core.common.MessageSourceService;
import com.krista.spring.boot.base.core.util.ResultUtil;
import com.krista.spring.boot.dto.annotation.SysLogger;
import com.krista.spring.boot.dto.result.Result;
import com.krista.spring.boot.dto.result.ResultCode;
import com.krista.spring.boot.service.SysUserRoleService;
import com.krista.spring.boot.service.SysUserService;
import com.krista.spring.boot.utils.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.krista.spring.boot.dto.user.PasswordDto;
import com.krista.spring.boot.dto.user.UserPageVo;
import com.krista.spring.boot.dto.user.UserUpdateVo;
import com.krista.spring.boot.dto.user.UserVo;
import com.krista.spring.boot.model.SysUser;
import com.krista.spring.boot.utils.BeanConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;


/**
 * SysUser
 *
 * @author jeikerxiao
 * @version 2018/06/27
 */
@Api(description = "用户管理")
@RestController
@RequestMapping("/sys/user")
@Slf4j
public class SysUserController extends AbstractController {

    @Resource
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private MessageSourceService messageSourceService;

    @SysLogger("保存用户")
    @PostMapping("/add")
    @RequiresPermissions("sys:user:add")
    public Result add(@Valid @RequestBody SysUser sysUser) {
        sysUser.setCreateUserId(getUserId());
        sysUserService.add(sysUser);
        return ResultUtil.success();
    }

    @SysLogger("删除用户")
    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public Result delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, Constant.SUPER_ADMIN)) {
            return ResultUtil.failure("系统管理员不能删除");
        }
        if (ArrayUtils.contains(userIds, getUserId())) {
            return ResultUtil.failure("当前用户不能删除");
        }
        sysUserService.deleteByIds(userIds);
        return ResultUtil.success();
    }

    @SysLogger("修改用户")
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    public Result update(@Valid @RequestBody UserUpdateVo userUpdateVo) {
        SysUser sysUser = BeanConverter.copy(userUpdateVo,SysUser.class);
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateUserId(getUserId());

        sysUserService.updateUser(sysUser);

        return ResultUtil.success();
    }

    @GetMapping("/item/{userId}")
    @RequiresPermissions("sys:user:item")
    public Result item(@PathVariable("userId") Long userId) {
        SysUser sysUser = sysUserService.getUserWithRole(userId);

        return ResultUtil.success(BeanConverter.copy(sysUser, UserVo.class));
    }

    @PostMapping("/list")
    @RequiresPermissions("sys:user:list")
    public Result list(@RequestBody UserPageVo userPageVo) {
        PageHelper.startPage(userPageVo.getPage(), userPageVo.getSize());

        // 构造查询条件
        Condition condition = new Condition(SysUser.class);
        if(StringUtils.isNotEmpty(userPageVo.getUserName())){
            condition.createCriteria().andLike("username","%" + userPageVo.getUserName() + "%");
        }

        List<SysUser> list = sysUserService.findByExample(condition);

        // 不返回密码和盐信息
        PageInfo pageInfo = new PageInfo(BeanConverter.copy(list, UserVo.class));
        return ResultUtil.success(pageInfo);
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    public Result info() {
        return ResultUtil.success(getUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLogger("修改密码")
    @PostMapping("/password")
    public Result password(@RequestBody PasswordDto passwordDto) {
        //sha256加密
        String password = new Sha256Hash(passwordDto.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(passwordDto.getNewPassword(), getUser().getSalt()).toHex();
        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return ResultUtil.failure(ResultCode.USER_OLDPASSWORD_ERROR);
        }
        return ResultUtil.success();
    }

    @ApiOperation("测试国际化")
    @GetMapping("/test")
    public Result test() {
        String msg = messageSourceService.getMessage("hello.world");
        log.info("hello.world = {}", msg);
        return ResultUtil.success(msg);
    }
}
