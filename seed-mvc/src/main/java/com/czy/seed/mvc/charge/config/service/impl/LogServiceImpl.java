package com.czy.seed.mvc.charge.config.service.impl;

import com.czy.seed.mvc.base.service.impl.BaseServiceImpl;
import com.czy.seed.mvc.charge.config.entity.TblLog;

import com.czy.seed.mvc.charge.config.mapper.LogMapper;
import com.czy.seed.mvc.charge.config.service.LogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/20.
 */
@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<TblLog> implements LogService{
    @Autowired
    private LogMapper logMapper;
}
