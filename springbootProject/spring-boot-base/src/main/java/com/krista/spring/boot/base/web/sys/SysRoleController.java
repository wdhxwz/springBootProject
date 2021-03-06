package com.krista.spring.boot.base.web.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.krista.spring.boot.base.core.util.ResultUtil;
import com.krista.spring.boot.dto.annotation.SysLogger;
import com.krista.spring.boot.dto.result.Result;
import com.krista.spring.boot.service.SysRoleMenuService;
import com.krista.spring.boot.service.SysRoleService;
import com.krista.spring.boot.base.core.util.ResultUtil;
import io.swagger.annotations.Api;
import com.krista.spring.boot.dto.common.PageVo;
import com.krista.spring.boot.model.SysRole;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * SysRole
 *
 * @author jeikerxiao
 * @version 2018/06/27
 */
@Api("角色管理")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @SysLogger("保存角色")
    @PostMapping("/add")
    @RequiresPermissions("sys:role:add")
    public Result add(@Valid @RequestBody SysRole sysRole) {

        sysRole.setCreateUserId(getUserId());
        sysRoleService.saveRole(sysRole);
        return ResultUtil.success();
    }

    @SysLogger("删除角色")
    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public Result delete(@RequestBody Long[] roleIds) {
        sysRoleService.deleteBatch(roleIds);
        return ResultUtil.success();
    }

    @SysLogger("修改角色")
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    public Result update(@Valid @RequestBody SysRole sysRole) {

        sysRole.setCreateUserId(getUserId());
        sysRoleService.updateRole(sysRole);
        return ResultUtil.success();
    }

    @GetMapping("/item/{roleId}")
    @RequiresPermissions("sys:role:item")
    public Result item(@PathVariable("roleId") Long roleId) {
        SysRole sysRole = sysRoleService.findById(roleId);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        sysRole.setMenuIdList(menuIdList);

        return ResultUtil.success(sysRole);
    }

    @PostMapping("/list")
    @RequiresPermissions("sys:role:list")
    public Result list(@RequestBody PageVo pageVo) {
        //如果不是超级管理员，则只查询自己创建的角色列表

        PageHelper.startPage(pageVo.getPage(), pageVo.getSize());
        List<SysRole> list = sysRoleService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultUtil.success(pageInfo);
    }

    @GetMapping("/select")
    @RequiresPermissions("sys:role:select")
    public Result select() {
        //如果不是超级管理员，则只查询自己所拥有的角色列表

        List<SysRole> list = sysRoleService.findAll();
        return ResultUtil.success(list);
    }
}
