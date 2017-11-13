package com.czy.seed.mvc.charge.config.service.impl;



import com.czy.seed.mvc.charge.config.service.ProductService;
import com.czy.seed.mvc.base.service.impl.BaseServiceImpl;
import com.czy.seed.mvc.charge.config.entity.TblOrderDetail;
import com.czy.seed.mvc.charge.config.entity.TblProduct;
import com.czy.seed.mvc.charge.config.mapper.ProductMapper;
import com.czy.seed.mybatis.base.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuyun on 2017/4/27.
 */

@Service("productService")
public class ProductServiceImpl extends BaseServiceImpl<TblProduct> implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    /**
     * 查询座位收费配置信息
     * @param
     * @return
     */
    public List<TblProduct> seatProductList() {
        QueryParams qp = new QueryParams(TblOrderDetail.class);

        return productMapper.selectListByParams(qp);
    }

    @Override
    public List<TblProduct> selectList(TblProduct product,int pageSize,int pageNum) {
        QueryParams qp = new QueryParams(TblProduct.class);
        QueryParams.Criteria criteria = qp.createCriteria();
        if (product.getProductNum() != null && !"".equals(product.getProductNum())) {
            criteria.andEqualTo("productNum", product.getProductNum());
        }

        if (product.getProductName() != null && !"".equals(product.getProductName())) {
            criteria.andLike("productName", "%"+product.getProductName()+"%");
        }

        if (product.getProductType() != null && !"".equals(product.getProductType())) {
            criteria.andEqualTo("productType", product.getProductType());
        }
        criteria.andEqualTo("status", 1);
//        List<TblProduct> products = productMapper.selectListByParams(qp);
        List<TblProduct> products = this.selectPageByParams(pageNum,pageSize, qp);
        return products;
    }

    @Override
    public List<String> selectProductType() {
        List<String> types = productMapper.selectProductType();
        return types;
    }

}
