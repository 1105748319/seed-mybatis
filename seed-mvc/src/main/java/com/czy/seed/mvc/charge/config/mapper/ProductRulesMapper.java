package com.czy.seed.mvc.charge.config.mapper;


import com.czy.seed.mvc.charge.config.entity.TblProductRules;
import com.czy.seed.mybatis.base.mapper.BaseMapper;
import com.czy.seed.mybatis.config.mybatis.annotations.AutoMapper;

/**
 * Created by Administrator on 2017/4/27.
 */

@AutoMapper()
public interface ProductRulesMapper extends BaseMapper<TblProductRules> {
    void updateProductRulesStatus(Integer productId);
}
