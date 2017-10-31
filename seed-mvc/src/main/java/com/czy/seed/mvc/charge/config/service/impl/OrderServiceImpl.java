package com.czy.seed.mvc.charge.config.service.impl;


import com.czy.seed.mvc.base.service.impl.BaseServiceImpl;
import com.czy.seed.mvc.charge.config.entity.TblOrder;
import com.czy.seed.mvc.charge.config.entity.TblOrderDetail;
import com.czy.seed.mvc.charge.config.entity.TblProduct;
import com.czy.seed.mvc.charge.config.mapper.OrderDetailMapper;
import com.czy.seed.mvc.charge.config.mapper.OrderMapper;
import com.czy.seed.mvc.charge.config.service.OrderDetailService;
import com.czy.seed.mvc.charge.config.service.OrderService;
import com.czy.seed.mvc.charge.config.util.PropertiesUtil;
import com.czy.seed.mybatis.base.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by liuyun on 2017/4/27.
 */
@EnableTransactionManagement
@Service
public class OrderServiceImpl extends BaseServiceImpl<TblOrder> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    private static String orderAffirm = PropertiesUtil.getStringProperty("orderAffirm");//行李收费


    /**
     * liuyun
     * 添加订单列表
     */
    @Override
    public int orderAdd(TblOrder order) {
        return 0;
    }

    /**
     * 删除订单列表
     *
     * @param id
     * @return
     */
    @Override
    public int orderDel(List<Integer> id) {
        return 0;
    }

    @Override
    public List<TblOrder> orderParamList(TblOrder order) {
        QueryParams qp = new QueryParams(TblOrder.class);
        QueryParams.Criteria criteria = qp.createCriteria();
        criteria.andEqualTo("orderNum", order.getOrderNum());//航班序号
        List<TblOrder> orderList = orderMapper.selectListByParams(qp);
        return orderList;
    }


    @Transactional
    public void orderSave(TblOrder orders, List<TblOrderDetail> orderDetailList) {
        int i;
        if ("Y".equals(orderAffirm)) {
            orders.setPaymentType("1");
            orders.setActualAmount(orders.getReceivableAmount());
            orders.setType("Y");
            i = orderMapper.insert(orders);
        } else {
            i = orderMapper.insert(orders);
        }
        if (i > 0) {
            for (TblOrderDetail orderDetail : orderDetailList) {
                orderDetail.setOrderId(orders.getId());
            }
            int j = orderDetailMapper.insertList(orderDetailList);
        }

    }


}
