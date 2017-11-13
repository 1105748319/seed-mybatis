package com.czy.seed.mvc.charge.config.controller;


import com.czy.seed.mvc.charge.config.entity.TblLog;
import com.czy.seed.mvc.charge.config.service.LogService;
import com.czy.seed.mvc.util.Res;


import com.czy.seed.mybatis.base.QueryParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 004410 on 2017/7/20.
 * 日志信息维护
 */
@RestController
@RequestMapping("/charge/log")
public class ChargeLogController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeLogController.class);
    @Autowired
    private LogService logService;


    @RequestMapping("/index")
    public ModelAndView collect() {
        return new ModelAndView("/charge/log");
    }

    @RequestMapping("/queryList")
    public Res queryList(TblLog tblLog, String pageSize, String pageNum) {
        try {
            int pSize = 1;
            int pNum = 1;
            pSize = Integer.valueOf(pageSize);
            pNum = Integer.valueOf(pageNum);
            QueryParams qp = new QueryParams(TblLog.class);
            QueryParams.Criteria criteria = qp.createCriteria();
            if (tblLog.getName() != null && !"".equals(tblLog.getName())) {
                criteria.andLike("name", "%"+tblLog.getName()+"%");
            }
            if (tblLog.getPassenger() != null && !"".equals(tblLog.getPassenger())) {
                criteria.andLike("passenger", "%"+tblLog.getPassenger()+"%");
            }
            List<TblLog> TblLogList = logService.selectPageByParams(pNum, pSize, qp);
            int i = logService.selectCountByParams(qp);
            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("logData", TblLogList);
            pageInfo.put("total", i);
            return Res.ok(pageInfo);
        } catch (Exception e) {
            logger.error("--query TblLogList failure!");
            return Res.error("query TblLogList failure!");
        }

    }

}
