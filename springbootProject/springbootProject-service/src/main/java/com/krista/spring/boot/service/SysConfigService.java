package com.krista.spring.boot.service;

import com.krista.spring.boot.model.SysConfig;

import java.util.List;


/**
 * SysConfig
 *
 * @version 2018/06/27
 * @author jeikerxiao
 */
public interface SysConfigService extends Service<SysConfig> {
    void add(SysConfig sysConfig);
    void batchDelete(List<String> ids);
}
