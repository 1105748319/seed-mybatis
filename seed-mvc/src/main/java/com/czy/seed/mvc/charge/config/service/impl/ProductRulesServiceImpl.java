package com.czy.seed.mvc.charge.config.service.impl;




import com.czy.seed.mvc.charge.config.entity.TblProductRules;
import com.czy.seed.mvc.charge.config.service.ProductRulesService;
import com.czy.seed.mvc.base.service.impl.BaseServiceImpl;
import com.czy.seed.mvc.charge.config.mapper.ProductRulesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuyun on 2017/4/27.
 */
@Service
public class ProductRulesServiceImpl extends BaseServiceImpl<TblProductRules> implements ProductRulesService {

    @Autowired
    private ProductRulesMapper productRulesMapper;

    @Override
    public void updateProductRulesStatus(Integer productId) {
        productRulesMapper.updateProductRulesStatus(productId);
    }
}
