package com.krista.spring.boot.service.impl;

import com.krista.spring.boot.dao.SysUserTokenMapper;
import com.krista.spring.boot.dao.redis.RedisUtils;
import com.krista.spring.boot.model.SysUserToken;
import com.krista.spring.boot.service.AbstractService;
import com.krista.spring.boot.service.SysUserTokenService;
import com.krista.spring.boot.service.shiro.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


/**
 * SysUserToken
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
@Service
@Transactional
public class SysUserTokenServiceImpl extends AbstractService<SysUserToken> implements SysUserTokenService {

    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Resource
    private SysUserTokenMapper sysUserTokenMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public SysUserToken createToken(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        //判断是否生成过token
        SysUserToken userToken = sysUserTokenMapper.selectByPrimaryKey(userId);
        if(userToken == null){
            userToken = new SysUserToken();
            userToken.setUserId(userId);
            userToken.setToken(token);
            userToken.setUpdateTime(now);
            userToken.setExpireTime(expireTime);
            //保存token
            sysUserTokenMapper.insert(userToken);
        }else{
            userToken.setToken(token);
            userToken.setUpdateTime(now);
            userToken.setExpireTime(expireTime);
            //更新token
            sysUserTokenMapper.updateByPrimaryKeySelective(userToken);
        }

        redisUtils.set(token,userToken,EXPIRE);


        return userToken;
    }

    @Override
    public void logout(long userId) {
        // 生成一个token
        String token = TokenGenerator.generateValue();

        // 修改token
        SysUserToken userToken = new SysUserToken();
        userToken.setUserId(userId);
        userToken.setToken(token);
        sysUserTokenMapper.updateByPrimaryKeySelective(userToken);
    }
}
