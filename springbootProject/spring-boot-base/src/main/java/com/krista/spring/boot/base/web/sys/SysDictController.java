package com.krista.spring.boot.base.web.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.krista.spring.boot.base.core.util.ResultUtil;
import com.krista.spring.boot.dto.common.IdVo;
import com.krista.spring.boot.dto.common.PageVo;
import com.krista.spring.boot.dto.result.Result;
import com.krista.spring.boot.model.SysDict;
import com.krista.spring.boot.service.SysDictService;
import com.krista.spring.boot.base.core.util.ResultUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * SysDict
 *
 * @version 2018/07/06
 * @author jeikerxiao
 */
@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    @Resource
    private SysDictService sysDictService;

    @PostMapping("/add")
    public Result add(@RequestBody SysDict sysDict) {
        sysDictService.save(sysDict);
        return ResultUtil.success();
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody IdVo idVo) {
        sysDictService.deleteById(idVo.getId());
        return ResultUtil.success();
    }

    @PostMapping("/update")
    public Result update(@RequestBody SysDict sysDict) {
        sysDictService.update(sysDict);
        return ResultUtil.success();
    }

    @PostMapping("/item")
    public Result item(@RequestBody IdVo idVo) {
        SysDict sysDict = sysDictService.findById(idVo.getId());
        return ResultUtil.success(sysDict);
    }

    @PostMapping("/list")
    public Result list(@RequestBody PageVo pageVo) {
        PageHelper.startPage(pageVo.getPage(), pageVo.getSize());
        List<SysDict> list = sysDictService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultUtil.success(pageInfo);
    }
}
