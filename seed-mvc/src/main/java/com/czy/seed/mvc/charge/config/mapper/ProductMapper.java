package com.czy.seed.mvc.charge.config.mapper;


import com.czy.seed.mvc.charge.config.entity.TblProduct;
import com.czy.seed.mybatis.base.mapper.BaseMapper;
import com.czy.seed.mybatis.config.mybatis.annotations.AutoMapper;

import java.util.List;

/**
 * Created by liuyun on 2017/4/27.
 */

@AutoMapper()
public interface ProductMapper extends BaseMapper<TblProduct> {

    List<String> selectProductType();

}
