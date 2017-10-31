package com.czy.seed.mvc.charge.config.controller;


import com.czy.seed.mvc.charge.config.entity.TblOrder;
import com.czy.seed.mvc.charge.config.entity.TblOrderDetail;
import com.czy.seed.mvc.charge.config.service.OrderDetailService;
import com.czy.seed.mvc.charge.config.service.OrderService;
import com.czy.seed.mvc.util.PrincipalUtil;
import com.czy.seed.mvc.util.Res;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 监控页面交互
 * Created by liuyun on 2017/04/28 .
 */

@Controller
@RequestMapping("/charge/orderDetail")
public class ChargeOrderDetailController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeOrderDetailController.class);

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderService orderService;

    //  从 application.properties 中读取配置，如取不到默认值为Hello Shanhy
    @Value("${application.hello:Hello Angel}")
    private String hello;


    /**
     * liuyun
     * 添加收费信息
     *
     * @return
     */
    @RequestMapping("/insert.do")
    @ResponseBody
    public Object insert(TblOrderDetail orderDetail) {
        String res = "";
        orderDetail.setCreatedDt(new Date());
        orderDetail.setUpdatedDt(new Date());
        try {
            List<TblOrderDetail> orderDetailList = new ArrayList<TblOrderDetail>();
            orderDetailList.add(orderDetail);
            int i = orderDetailService.OrderDetailAddOne(orderDetail);
            res = "添加收费信息成功！" + "插入收费信息" + i + "条";

        } catch (Exception e) {
            res = "插入信息失败!";
            logger.error("插入收费信息失败！", e);
        }
        return res;
    }


    @RequestMapping("/orderConfirmation")
    public ModelAndView orderConfirmation() {
        ModelAndView view = new ModelAndView("/charge/orderConfirmation");
        view.addObject("name", PrincipalUtil.getLoginUser().getUsername());
        return view;
    }


    @RequestMapping("/orderConfirmationList")
    @ResponseBody
    public Res orderConfirmationList(TblOrderDetail tblOrderDetail) {
        try {
            List<TblOrderDetail> orderConfirmationList = orderDetailService.OrderDetailMapperList(tblOrderDetail);
            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("orderConfirmationList", orderConfirmationList);

            return Res.ok(pageInfo);
        } catch (Exception e) {
            logger.error("orderConfirmation error!");
            return Res.error();
        }

    }

    @RequestMapping("/personalDetails")
    @ResponseBody
    public Res personalDetails(TblOrder order) {
        Map<String, Object> pageInfo = new HashMap<>();
        try {
            List<TblOrder> orders = orderService.orderParamList(order);
            if (orders == null && orders.size() == 0) {
                logger.warn("没有查询到订单信息!");
                throw new Exception("没有查询到订单信息!");
            }
            pageInfo.put("order", orders.get(0));
            return Res.ok(pageInfo);
        } catch (Exception e) {
            logger.info("");
            return Res.error();
        }


    }


}
