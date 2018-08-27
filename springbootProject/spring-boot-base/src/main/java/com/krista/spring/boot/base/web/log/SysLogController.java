package com.krista.spring.boot.base.web.log;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.krista.spring.boot.base.core.util.ResultUtil;
import com.krista.spring.boot.dto.common.IdVo;
import com.krista.spring.boot.dto.common.PageVo;
import com.krista.spring.boot.dto.log.SysLogDto;
import com.krista.spring.boot.dto.result.Result;
import com.krista.spring.boot.model.SysLog;
import com.krista.spring.boot.service.SysLogService;
import com.krista.spring.boot.base.core.util.ResultUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统日志
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @PostMapping("/item")
    public Result item(@RequestBody IdVo idVo) {
        SysLog sysLog = sysLogService.findById(idVo.getId());
        SysLogDto sysLogDto = new SysLogDto();
        BeanUtils.copyProperties(sysLog, sysLogDto);
        return ResultUtil.success(sysLogDto);
    }

    @PostMapping("/list")
    @RequiresPermissions("sys:log:list")
    public Result list(@RequestBody PageVo pageVo) {
        PageHelper.startPage(pageVo.getPage(), pageVo.getSize());
        Example condition = new Example(SysLog.class);
        condition.orderBy("createDate").desc();
        List<SysLog> list = sysLogService.findByExample(condition);
        PageInfo pageInfo = new PageInfo(list);
        return ResultUtil.success(pageInfo);
    }
}
