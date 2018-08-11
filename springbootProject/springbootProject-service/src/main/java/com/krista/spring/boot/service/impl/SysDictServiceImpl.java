package com.krista.spring.boot.service.impl;

import com.krista.spring.boot.dao.SysDictMapper;
import com.krista.spring.boot.model.SysDict;
import com.krista.spring.boot.service.AbstractService;
import com.krista.spring.boot.service.SysDictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * SysDict
 *
 * @version 2018/07/06
 * @author jeikerxiao
 */
@Service
@Transactional
public class SysDictServiceImpl extends AbstractService<SysDict> implements SysDictService {

    @Resource
    private SysDictMapper sysDictMapper;

}
