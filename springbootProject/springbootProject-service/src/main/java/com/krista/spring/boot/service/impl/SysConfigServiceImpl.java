package com.krista.spring.boot.service.impl;

import com.krista.spring.boot.dao.SysConfigMapper;
import com.krista.spring.boot.dto.exception.ServiceException;
import com.krista.spring.boot.dto.result.ResultCode;
import com.krista.spring.boot.model.SysConfig;
import com.krista.spring.boot.service.AbstractService;
import com.krista.spring.boot.service.SysConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


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

    @Override
    public void add(SysConfig sysConfig) {
        SysConfig config = this.findBy("paramKey",sysConfig.getParamKey());
        if(config != null){
            throw new ServiceException(ResultCode.PARAMKEY_EXIST);
        }

        this.save(sysConfig);
    }

    @Transactional
    @Override
    public void batchDelete(List<String> ids) {
        for (String id : ids) {
            this.deleteById(Long.valueOf(id));
        }
    }
}
