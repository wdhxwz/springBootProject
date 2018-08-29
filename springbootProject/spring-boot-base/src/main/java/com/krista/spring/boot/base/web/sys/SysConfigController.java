package com.krista.spring.boot.base.web.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.krista.spring.boot.base.core.util.ResultUtil;
import com.krista.spring.boot.dto.annotation.SysLogger;
import com.krista.spring.boot.dto.result.Result;
import com.krista.spring.boot.service.SysConfigService;
import io.swagger.annotations.Api;
import com.krista.spring.boot.dto.common.IdVo;
import com.krista.spring.boot.dto.common.PageVo;
import com.krista.spring.boot.model.SysConfig;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * SysConfig
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
@Api("配置管理")
@RestController
@RequestMapping("/sys/config")
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;

    @SysLogger("新增配置")
    @PostMapping("/add")
    @RequiresPermissions("sys:config:add")
    public Result add(@RequestBody SysConfig sysConfig) {
        sysConfigService.add(sysConfig);
        return ResultUtil.success();
    }

    @SysLogger("删除配置")
    @PostMapping("/delete")
    @RequiresPermissions("sys:config:delete")
    public Result delete(@RequestBody List<String> ids) {
        sysConfigService.batchDelete(ids);
        return ResultUtil.success();
    }

    @SysLogger("修改配置")
    @PostMapping("/update")
    @RequiresPermissions("sys:config:update")
    public Result update(@RequestBody SysConfig sysConfig) {
        sysConfigService.update(sysConfig);
        return ResultUtil.success();
    }

    @PostMapping("/item")
    @RequiresPermissions("sys:config:item")
    public Result item(@RequestBody IdVo idVo) {
        SysConfig sysConfig = sysConfigService.findById(idVo.getId());
        return ResultUtil.success(sysConfig);
    }

    @PostMapping("/list")
    @RequiresPermissions("sys:config:list")
    public Result list(@RequestBody PageVo pageVo) {
        PageHelper.startPage(pageVo.getPage(), pageVo.getSize());
        List<SysConfig> list = sysConfigService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultUtil.success(pageInfo);
    }

    @GetMapping("/sysConfig")
    public Result sysConfig(){
        SysConfig sysConfig = sysConfigService.findBy("paramKey","projectName");
        return ResultUtil.success(sysConfig);
    }
}
