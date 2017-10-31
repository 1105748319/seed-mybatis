package com.czy.seed.mvc.charge.config.service.impl;

import com.czy.seed.mvc.charge.config.mapper.ReportMapper;
import com.czy.seed.mvc.charge.config.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/7/13.
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<Map<String, Object>> selectCollectGroupNum(Map<String, String> params) {
        List<Map<String, Object>> result = reportMapper.selectCollectGroupNum(params);

        for (Map<String, Object> m : result) {
            if ("OTHERS".equals(m.get("productType")) || "BAGGAGE(OTHERS)".equals(m.get("productType"))) {
                m.put("productNum", ((Double) m.get("unitSum")).longValue());
            }
        }

        if (!"1".equals(params.get("group"))) {
            for (Map<String, Object> m : result) {
                m.put("createBy", "");
            }
        }


        return result;
    }

    @Override
    public List<Map<String, Object>> selectPersonDetail(Map<String, Object> params) {
        List<Map<String, Object>> result = reportMapper.selectPersonDetail(params);

        return result;
    }

    @Override
    public List<Map<String, Object>> selectFlightDetail(Map<String, Object> params) {
        List<Map<String, Object>> list = reportMapper.selectFlightDetail(params);

        return list;
    }
}
