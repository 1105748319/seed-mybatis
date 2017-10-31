package com.czy.seed.mvc.charge.config.service;
import com.czy.seed.mvc.base.service.BaseService;
import com.czy.seed.mvc.charge.config.entity.TblOrder;
import com.czy.seed.mvc.charge.config.entity.TblOrderDetail;
import com.czy.seed.mvc.charge.config.entity.TblProduct;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by liuyun  on 2017/4/27.
 */
public interface OrderService extends BaseService<TblOrder> {





    /**
     * liuyun
     * 添加订单列表
     *
     */
    public int orderAdd(TblOrder order);

    /**
     * 删除订单列表
     * @param id
     * @return
     */
    public int orderDel(List<Integer> id);

    public List<TblOrder> orderParamList(TblOrder order);

    /**
     * 保存订单详细 事物
     * @param orders
     * @param orderDetailList
     */
    public void orderSave(TblOrder orders,List<TblOrderDetail> orderDetailList);
}


