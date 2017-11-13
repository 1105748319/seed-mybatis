package com.czy.seed.mvc.charge.config.mapper;

import com.czy.seed.mvc.charge.config.entity.ReportVo;
import com.czy.seed.mybatis.base.mapper.BaseMapper;
import com.czy.seed.mybatis.config.mybatis.annotations.AutoMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/7/12.
 */
@AutoMapper
public interface ReportMapper extends BaseMapper<ReportVo> {

    List<Map<String, Object>> selectCollectGroupNum(Map<String, String> params);

    Map<String, Object> selectCollectGroupType(Map<String, String> params);

    List<Map<String, Object>> selectPersonDetail(Map<String, Object> params);

    List<Map<String, Object>> selectFlightDetail(Map<String, Object> params);
}
