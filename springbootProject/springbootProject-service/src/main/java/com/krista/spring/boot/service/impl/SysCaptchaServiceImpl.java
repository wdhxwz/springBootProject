package com.krista.spring.boot.service.impl;

import com.krista.spring.boot.dao.SysCaptchaMapper;
import com.krista.spring.boot.model.SysCaptcha;
import com.krista.spring.boot.service.AbstractService;
import com.krista.spring.boot.service.SysCaptchaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * SysCaptcha
 *
 * @version 2018/07/02
 * @author jeikerxiao
 */
@Service
@Transactional
public class SysCaptchaServiceImpl extends AbstractService<SysCaptcha> implements SysCaptchaService {

    @Resource
    private SysCaptchaMapper sysCaptchaMapper;

}
