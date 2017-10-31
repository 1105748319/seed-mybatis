package com.czy.seed.mvc.charge.config.service;

import com.czy.seed.mvc.base.service.BaseService;
import com.czy.seed.mvc.charge.config.entity.TblProductRules;

/**
 * Created by liuyun on 2017/4/27.
 */
public interface ProductRulesService extends BaseService<TblProductRules> {
    void updateProductRulesStatus(Integer productId);
}
