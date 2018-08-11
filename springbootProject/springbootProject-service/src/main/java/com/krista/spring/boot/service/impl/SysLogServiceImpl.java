package com.krista.spring.boot.service.impl;

import com.krista.spring.boot.dao.SysLogMapper;
import com.krista.spring.boot.model.SysLog;
import com.krista.spring.boot.service.AbstractService;
import com.krista.spring.boot.service.SysLogService;
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
