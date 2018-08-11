package com.krista.spring.boot.service.impl;

import com.krista.spring.boot.dao.SysConfigMapper;
import com.krista.spring.boot.model.SysConfig;
import com.krista.spring.boot.service.AbstractService;
import com.krista.spring.boot.service.SysConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * SysConfig
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
@Service
@Transactional
public class SysConfigServiceImpl extends AbstractService<SysConfig> implements SysConfigService {

    @Resource
    private SysConfigMapper sysConfigMapper;

}
