package com.sinocare.base.service.impl.sys;

import com.sinocare.base.core.AbstractService;
import com.krista.spring.boot.dao.SysCaptchaMapper;
import com.sinocare.base.service.sys.SysCaptchaService;
import com.krista.spring.boot.model.SysCaptcha;
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
