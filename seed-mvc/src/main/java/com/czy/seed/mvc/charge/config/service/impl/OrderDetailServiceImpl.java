package com.czy.seed.mvc.charge.config.service.impl;
import com.czy.seed.mvc.base.service.BaseService;
import com.czy.seed.mvc.base.service.impl.BaseServiceImpl;
import com.czy.seed.mvc.charge.config.entity.TblOrder;
import com.czy.seed.mvc.charge.config.entity.TblOrderDetail;
import com.czy.seed.mvc.charge.config.mapper.OrderDetailMapper;
import com.czy.seed.mvc.charge.config.service.OrderDetailService;
import com.czy.seed.mybatis.base.QueryParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by Administrator on 2017/4/27.
 */

@Service("orderDetailService")
public class OrderDetailServiceImpl extends BaseServiceImpl<TblOrderDetail> implements OrderDetailService {
    private Logger logger = LoggerFactory.getLogger(OrderDetailServiceImpl.class);

    @Autowired
    private OrderDetailMapper OrderDetailMapper;

    /**
     * liuyun
     * 更具条件查询数据信息
     * @param tblOrderDetail
     * @return
     */

    public List<TblOrderDetail> OrderDetailMapperList(TblOrderDetail tblOrderDetail){
        QueryParams qp = new QueryParams(TblOrderDetail.class);//TODO 需要校验旅客信息为那个
        QueryParams.Criteria criteria = qp.createCriteria();
        /*criteria.andEqualTo("fi", order.getFi());//航班序号
        criteria.andEqualTo("name", order.getName());//旅客姓名
        criteria.andEqualTo("licenseNo", order.getLicenseNo());//旅客证件号*/
        List<TblOrderDetail> orderDetailList = OrderDetailMapper.selectListByParams(qp);
        return orderDetailList;
    }

    /**
     * 批量插入信息
     * liuyun
     * @param tblOrderDetailList
     * @return
     */
    public int OrderDetailAddList(List<TblOrderDetail> tblOrderDetailList){
       int i=  OrderDetailMapper.insertList(tblOrderDetailList);
       return i;
    }

    /**
     *
     */
    public int OrderDetailAddOne(TblOrderDetail tblOrderDetail){
        int i = OrderDetailMapper.insert(tblOrderDetail);
        return i;
    }
}
