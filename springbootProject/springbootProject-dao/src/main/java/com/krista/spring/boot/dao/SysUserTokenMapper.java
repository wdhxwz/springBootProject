package com.krista.spring.boot.dao;

import com.krista.spring.boot.MyMapper;
import com.krista.spring.boot.model.SysUserToken;

public interface SysUserTokenMapper extends MyMapper<SysUserToken> {

    SysUserToken queryByToken(String token);

}