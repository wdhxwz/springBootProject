package com.krista.spring.boot.base.web.sys;

import com.krista.spring.boot.base.core.util.ResultUtil;
import com.krista.spring.boot.dto.annotation.SysLogger;
import com.krista.spring.boot.dto.result.Result;
import com.krista.spring.boot.service.SysUserService;
import com.krista.spring.boot.service.SysUserTokenService;
import com.krista.spring.boot.base.core.util.ResultUtil;
import io.swagger.annotations.Api;
import com.krista.spring.boot.dto.user.LoginDto;
import com.krista.spring.boot.model.SysUser;
import com.krista.spring.boot.model.SysUserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Description: spring-boot-base
 * Created by jeikerxiao on 2018/6/27 下午4:11
 */
@Api("登录管理")
@RestController
@RequestMapping("/sys/user")
@Slf4j
public class SysLoginController extends AbstractController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    /**
     * 登录
     */
    @PostMapping("/login")
    @SysLogger("用户登录")
    public Result login(@Valid @RequestBody LoginDto loginDto) {
        log.info("user login: {}", loginDto.getUsername());
        // 用户信息
        SysUser user = sysUserService.queryByUserName(loginDto.getUsername());

        // 账号不存在、密码错误
        if (user == null || !user.getPassword().equals(new Sha256Hash(loginDto.getPassword(), user.getSalt()).toHex())) {
            return ResultUtil.failure("账号或密码不正确");
        }
        // 账号锁定
        if (user.getStatus() == 0) {
            return ResultUtil.failure("账号已被锁定,请联系管理员");
        }
        // 生成token，并保存到数据库
        SysUserToken userToken = sysUserTokenService.createToken(user.getUserId());
        return ResultUtil.success(userToken);
    }

    /**
     * 退出
     */
    @SysLogger("用户登出")
    @PostMapping("/logout")
    public Result logout() {
        log.info("user logout: {}", getUserId());
        sysUserTokenService.logout(getUserId());
        return ResultUtil.success();
    }
}
