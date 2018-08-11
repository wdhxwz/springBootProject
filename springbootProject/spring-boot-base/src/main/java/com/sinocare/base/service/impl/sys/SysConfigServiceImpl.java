package com.sinocare.base.service.impl.sys;

import com.sinocare.base.core.AbstractService;
import com.krista.spring.boot.dao.SysConfigMapper;
import com.sinocare.base.service.sys.SysConfigService;
import com.krista.spring.boot.model.SysConfig;
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
