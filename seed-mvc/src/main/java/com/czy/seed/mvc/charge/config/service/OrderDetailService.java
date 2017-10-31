package com.czy.seed.mvc.charge.config.service;



import com.czy.seed.mvc.base.service.BaseService;
import com.czy.seed.mvc.charge.config.entity.TblOrder;
import com.czy.seed.mvc.charge.config.entity.TblOrderDetail;

import java.util.List;

/**
 * Created by liuyun on 2017/4/27.
 */
public interface OrderDetailService extends BaseService<TblOrderDetail> {
    public List<TblOrderDetail> OrderDetailMapperList(TblOrderDetail tblOrderDetail);

    public int OrderDetailAddList(List<TblOrderDetail> tblOrderDetailList);

    public int OrderDetailAddOne(TblOrderDetail tblOrderDetail);
}
