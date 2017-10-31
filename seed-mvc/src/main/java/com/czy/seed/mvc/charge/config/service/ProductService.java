package com.czy.seed.mvc.charge.config.service;



import com.czy.seed.mvc.base.service.BaseService;
import com.czy.seed.mvc.charge.config.entity.TblProduct;

import java.util.List;

/**
 * Created by liuyun on 2017/4/27.
 */

public interface ProductService extends BaseService<TblProduct> {

    /**
     * 查询产品列表
     * @param product
     * @return
     */
    public List<TblProduct> selectList(TblProduct product,int pageSize,int pageNum);

    /**
     * 查询所有的产品类型
     * @return
     */
    public List<String> selectProductType();
}

