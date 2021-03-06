package com.krista.spring.boot.base.core.oauth2;

import com.krista.spring.boot.model.SysUser;
import com.krista.spring.boot.model.SysUserToken;
import com.krista.spring.boot.service.ShiroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Description: 授权 Realm
 * Created by jeikerxiao on 2018/6/27 下午4:46
 */
@Component
@Slf4j
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.debug("doGetAuthorizationInfo");

        SysUser user = (SysUser)principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        // 用户权限列表
        // TODO 权限可以进行缓存
        Set<String> permsSet = shiroService.getUserPermissions(userId);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.debug("doGetAuthenticationInfo");

        String accessToken = (String) token.getPrincipal();

        // 根据Token，查询用户信息
        // TODO : 可以将Token信息进行缓存
        // TODO : Token的有效期是否自动延期
        SysUserToken userToken = shiroService.queryByToken(accessToken);

        // token失效
        if(userToken == null || userToken.getExpireTime().getTime() < System.currentTimeMillis()){
            throw new IncorrectCredentialsException("token失效, 请重新登录");
        }

        // 查询用户信息
        // TODO 这里将用户的权限信息缓存起来
        SysUser user = shiroService.queryUser(userToken.getUserId());

        // 账号锁定
        if(user.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定, 请联系管理员");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
        return info;
    }
}
