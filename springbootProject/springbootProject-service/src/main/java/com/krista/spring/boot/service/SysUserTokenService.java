package com.krista.spring.boot.service;

import com.krista.spring.boot.model.SysUserToken;


/**
 * SysUserToken
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
public interface SysUserTokenService extends Service<SysUserToken> {

    /**
     * 生成token
     * @param userId  用户ID
     */
    SysUserToken createToken(long userId);

    /**
     * 退出，修改token值
     * @param userId  用户ID
     */
    void logout(long userId);
}
