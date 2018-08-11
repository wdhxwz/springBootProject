package com.sinocare.base.service.impl.log;

import com.sinocare.base.core.AbstractService;
import com.krista.spring.boot.dao.SysLogMapper;
import com.sinocare.base.service.log.SysLogService;
import com.krista.spring.boot.model.SysLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * SysLog
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
@Service
@Transactional
public class SysLogServiceImpl extends AbstractService<SysLog> implements SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

}
