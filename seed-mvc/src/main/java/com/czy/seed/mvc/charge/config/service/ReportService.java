package com.czy.seed.mvc.charge.config.service;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/7/12.
 */
public interface ReportService {

    List<Map<String, Object>> selectCollectGroupNum(Map<String, String> params);

    List<Map<String, Object>> selectPersonDetail(Map<String, Object> params);

    List<Map<String, Object>> selectFlightDetail(Map<String, Object> params);
}
