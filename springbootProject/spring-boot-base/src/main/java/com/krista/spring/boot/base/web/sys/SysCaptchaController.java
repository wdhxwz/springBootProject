package com.krista.spring.boot.base.web.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.krista.spring.boot.base.core.util.ResultUtil;
import com.krista.spring.boot.dto.common.IdVo;
import com.krista.spring.boot.dto.common.PageVo;
import com.krista.spring.boot.dto.result.Result;
import com.krista.spring.boot.model.SysCaptcha;
import com.krista.spring.boot.service.SysCaptchaService;
import com.krista.spring.boot.base.core.util.ResultUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * SysCaptcha
 *
 * @version 2018/07/02
 * @author jeikerxiao
 */
@RestController
@RequestMapping("/sys/captcha")
public class SysCaptchaController {

    @Resource
    private SysCaptchaService sysCaptchaService;

    @PostMapping("/add")
    public Result add(@RequestBody SysCaptcha sysCaptcha) {
        sysCaptchaService.save(sysCaptcha);
        return ResultUtil.success();
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody IdVo idVo) {
        sysCaptchaService.deleteById(idVo.getId());
        return ResultUtil.success();
    }

    @PostMapping("/update")
    public Result update(@RequestBody SysCaptcha sysCaptcha) {
        sysCaptchaService.update(sysCaptcha);
        return ResultUtil.success();
    }

    @PostMapping("/item")
    public Result item(@RequestBody IdVo idVo) {
        SysCaptcha sysCaptcha = sysCaptchaService.findById(idVo.getId());
        return ResultUtil.success(sysCaptcha);
    }

    @PostMapping("/list")
    public Result list(@RequestBody PageVo pageVo) {
        PageHelper.startPage(pageVo.getPage(), pageVo.getSize());
        List<SysCaptcha> list = sysCaptchaService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultUtil.success(pageInfo);
    }
}
