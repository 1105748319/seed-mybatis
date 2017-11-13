package com.czy.seed.mvc.charge.config.controller;


import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.czy.seed.mvc.charge.config.entity.TblLog;
import com.czy.seed.mvc.charge.config.entity.TblOrder;
import com.czy.seed.mvc.charge.config.entity.TblOrderDetail;
import com.czy.seed.mvc.charge.config.entity.TblProduct;
import com.czy.seed.mvc.charge.config.service.LogService;
import com.czy.seed.mvc.charge.config.service.OrderDetailService;
import com.czy.seed.mvc.charge.config.service.OrderService;
import com.czy.seed.mvc.charge.config.service.ProductService;
import com.czy.seed.mvc.charge.config.util.ChargeMap;
import com.czy.seed.mvc.charge.config.util.PropertiesUtil;
import com.czy.seed.mvc.util.Res;


import com.czy.seed.mybatis.base.QueryParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import java.math.BigDecimal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 页面交互
 * Created by 004410 on 2017/04/28 .
 * 订单信息
 */

@RestController
@RequestMapping("/charge/order")
public class ChargeOrderController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeOrderController.class);
    @Autowired
    private LogService logService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailService orderDetailService;
    private TblOrder Order;
    //  从 application.properties 中读取配置，如取不到默认值为Hello Shanhy
    @Value("${application.hello:Hello Angel}")
    private String hello;

    /**
     * 004410
     *
     * @param request 录入接口信息
     * @return 加载收费页面
     */

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("/charge/charge");
        try {

            TblOrder tblOrder = new TblOrder();
            tblOrder.setFi(request.getParameter("fi"));//航班号
            tblOrder.setIp(request.getParameter("ip"));//柜台
            tblOrder.setName(request.getParameter("name"));
            tblOrder.setCreatedBy(request.getParameter("createdBy"));
            tblOrder.setAirport(request.getParameter("airport"));
            tblOrder.setLicenseNo(request.getParameter("licenseNo"));
            tblOrder.setFid(request.getParameter("fid"));//旅客标识
            tblOrder.setDa(request.getParameter("da"));
            tblOrder.setFs(request.getParameter("fs"));
            tblOrder.setRl(request.getParameter("rl"));
            tblOrder.setFreeLuggageNum(Integer.valueOf(request.getParameter("freeLuggageNum")));//免费行李重量
            tblOrder.setNetBaggage(request.getParameter("netBaggage"));
            tblOrder.setSn(request.getParameter("seat"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
            Date date = null;
            try {
                date = sdf.parse(request.getParameter("fdy"));
                tblOrder.setFdy(date);
            } catch (ParseException e) {
                logger.error("航班日期格式转换失败!", e);
            }
            try {
                TblLog tblLog = new TblLog();
                tblLog.setFid(tblOrder.getFid());
                tblLog.setFi(tblOrder.getFi());
                tblLog.setFs(tblOrder.getFs());
                tblLog.setIp(tblOrder.getIp());
                tblLog.setMessage("Work number:" + tblOrder.getCreatedBy()  + ",request:" + tblOrder.getName() + ",fee information");
                tblLog.setName(tblOrder.getCreatedBy());
                tblLog.setDate(new Date());
                tblLog.setPassenger(tblOrder.getName());
                logService.insert(tblLog);
            } catch (Exception log) {
                logger.error("请求收费信息中日志信息转换失败!");
            }

            logger.info("接收到收费项目请求请求！");
            logger.info("工号:" + tblOrder.getCreatedBy() + ",在:" + new Date() + ",请求:" + tblOrder.getName() + "的收费信息");
            Order = tblOrder;
        } catch (Exception e) {
            logger.error("初始化收费页面数据失败！", e);
        }
        return view;
    }


    /**
     * 订单信息初始化
     * 004410
     *
     * @param
     * @return 订单确认页面
     */

    @RequestMapping("/orderMessage")
    public Res orderMessage() {
        //TODO 需要查询条件（旅客的具体信息）
        Res res = Res.ok();
        try {
            QueryParams orderQp = new QueryParams(TblOrder.class);
            if (Order == null) {
                return Res.error("错误的操作");
            }
            orderQp.createCriteria().andCondition("TBL_ORDER.FI=" + "'" + Order.getFi() + "' AND " + "TBL_ORDER.FID=" + "'" + Order.getFid() + "'");
            List<TblOrder> OrderList = orderService.selectListRelativeByParams(orderQp);//订单列表
            //查看具体规则信息
            QueryParams qp = new QueryParams(TblProduct.class);

            qp.createCriteria().andCondition(" TBLPRODUCTRULESLIST.FS = " + "'" + Order.getFs() + "' AND TBL_PRODUCT.STATUS = 1 " + "AND TBLPRODUCTRULESLIST.STATUS = 1");

            List<TblProduct> ProductList = productService.selectListRelativeByParams(qp);
            Map<String, Object> pageInfo = new HashMap<String, Object>();
            pageInfo.put("OrderList", OrderList);//订单列表
            pageInfo.put("chargeMap", ChargeMap.getCharge());//收费的项目集合
            pageInfo.put("ProductList", ProductList);//收费规则信息
            pageInfo.put("tblOrder", Order);//CKI数据
            pageInfo.put("CKIURL", PropertiesUtil.getStringProperty("CKIURL"));//CKI打印地址
            res = Res.ok(pageInfo);
            Order = null;
        } catch (Exception e) {
            res = Res.error("The system call interface:CKI request");
            logger.error("CKI调用收费页面异常", e);
        }
        return res;
    }

    /**
     * 004410
     * 添加收费信息
     *
     * @param request 前台传入的信息
     * @return 返回前台提示信息
     */

    @RequestMapping("/addData")
    public Res addData(HttpServletRequest request) {//TODO 保存数据要加事物
        Res res = Res.ok();
        int i = 0;
        try {

            String ord = request.getParameter("order");
            String detail = request.getParameter("detail");
            TblOrder order = JSONObject.parseObject(ord, TblOrder.class);
            List<TblOrderDetail> orderDetailList = (List<TblOrderDetail>) JSONObject.parseArray(detail, TblOrderDetail.class);
            TblOrder orders = orderData(order, orderDetailList);
            orders.setCreatedDt(new Date());//创建时间
            orderService.orderSave(orders, orderDetailList);
            Map<String, Object> pageInfo = new HashMap<String, Object>();
            QueryParams orderQp = new QueryParams(TblOrder.class);
            orderQp.createCriteria().andCondition("TBL_ORDER.FI=" + "'" + orders.getFi() + "' AND " + "TBL_ORDER.FID=" + "'" + orders.getFid() + "'");
            List<TblOrder> OrderList = orderService.selectListRelativeByParams(orderQp);//订单列表
            pageInfo.put("OrderList", OrderList);
            pageInfo.put("orderId", order.getId());
            res = Res.ok(pageInfo);
            //录入日志信息
            try {
                TblLog tblLog = new TblLog();
                tblLog.setFid(orders.getFid());
                tblLog.setFi(orders.getFi());
                tblLog.setFs(orders.getFs());
                tblLog.setIp(orders.getIp());
                tblLog.setName(orders.getCreatedBy());
                tblLog.setDate(new Date());
                tblLog.setPassenger(orders.getName());
                String baggageTotal = request.getParameter("baggageTotal");
                String seatTotal = "";
                String baggage_othersTotal = "";
                String otherTotal="";
                for (TblOrderDetail orderDetail : orderDetailList) {
                    if (ChargeMap.getCharge().get("seat").equals(orderDetail.getProductType())) {
                        seatTotal =seatTotal+";"+ orderDetail.getUnit()+"/"+orderDetail.getMoney();
                    } else if (ChargeMap.getCharge().get("baggage_others").equals(orderDetail.getProductType())) {
                        baggage_othersTotal =baggage_othersTotal+";"+ orderDetail.getUnit()+"/"+orderDetail.getMoney();
                    } else if (ChargeMap.getCharge().get("other").equals(orderDetail.getProductType())) {
                        otherTotal = otherTotal+";"+orderDetail.getProductName()+"/"+ orderDetail.getUnit()+"/"+orderDetail.getMoney();
                    }
                }

                String msg =  orders.getCreatedBy() + "  At the counter: " + orders.getIp() + " add " + orders.getName() + " fee information; "+"order number:" + orders.getOrderNum() +"Baggage:"+baggageTotal+"; seat:"+seatTotal +"; otherBaggage:"+baggage_othersTotal+"; other:"+otherTotal;

                tblLog.setMessage(msg);
                logService.insert(tblLog);
            } catch (Exception e) {
                logger.error("添加收费信息转换日志信息失败!", e);
            }

        } catch (Exception e) {
            logger.error("添加收费信息转换日志信息失败!", e);
            res = Res.error("DATA ERROR!");
        }

        return res;
    }

    /**
     * 订单数据信息转换
     * 004410
     *
     * @param order           CKI信息
     * @param orderDetailList 收费信息
     * @return 订单信息
     */

    public TblOrder orderData(TblOrder order, List<TblOrderDetail> orderDetailList) {
        boolean T = true;//判断订单号是否重复
        BigDecimal baggageNum = new BigDecimal("0.00");
        BigDecimal seatNum = new BigDecimal("0.00");
        BigDecimal bigbaggageNum = new BigDecimal("0.00");
        BigDecimal otherNum = new BigDecimal("0.00");

        order.setType("N"); //状态为未收费
        for (TblOrderDetail orderDetail : orderDetailList) {
            if (ChargeMap.getCharge().get("baggage").equals(orderDetail.getProductType())) {
                baggageNum = baggageNum.add(orderDetail.getMoney());
                order.setOverweightLuggage(baggageNum);
            } else if (ChargeMap.getCharge().get("seat").equals(orderDetail.getProductType())) {
                seatNum = seatNum.add(orderDetail.getMoney());
                order.setSeat(seatNum);
            } else if (ChargeMap.getCharge().get("baggage_others").equals(orderDetail.getProductType())) {
                bigbaggageNum = bigbaggageNum.add(orderDetail.getMoney());
                order.setSpecialLuggage(bigbaggageNum);
            } else if (ChargeMap.getCharge().get("other").equals(orderDetail.getProductType())) {
                otherNum = otherNum.add(orderDetail.getMoney());
                order.setOther(otherNum);
            } else {
                order.setOther(new BigDecimal("0.00"));
            }
        }
        order.setReceivableAmount(baggageNum.add(seatNum).add(bigbaggageNum).add(otherNum));
        order.setCreatedDt(new Date());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        order.setOrderNum(order.getAirport() + dateString + rannum);

        while (T) {//todo 走查看优化
            QueryParams orderQp = new QueryParams(TblOrder.class);
            orderQp.createCriteria().andEqualTo("orderNum", order.getAirport() + dateString + rannum);
            List<TblOrder> OrderList = orderService.selectListRelativeByParams(orderQp);//订单列表
            if (OrderList.size() > 0) {
                order.setOrderNum(order.getAirport() + dateString + (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000);
            } else {
                T = false;
            }
        }

        return order;
    }


    /**
     * 订单查询
     * 004410
     *
     * @param orderNum  订单号
     * @param updatedBy 修改人
     * @return 订单确认页面
     */

    @RequestMapping("/orderSelect")
    public Res orderSelect(String orderNum, String updatedBy) {
        Map<String, Object> pageInfo = new HashMap<String, Object>();
        try {
            QueryParams orderQp = new QueryParams(TblOrder.class);
            QueryParams.Criteria criteria = orderQp.createCriteria();
            criteria.andEqualTo("orderNum", orderNum);
            List<TblOrder> OrderList = orderService.selectListRelativeByParams(orderQp);//订单列表

            pageInfo.put("updatedBy", updatedBy);
            pageInfo.put("chargeMap", ChargeMap.getCharge());
            pageInfo.put("OrderList", OrderList);
            try {
                TblLog tblLog = new TblLog();
                tblLog.setName(updatedBy);
                tblLog.setDate(new Date());
                if(OrderList.size()>0){
                    tblLog.setPassenger(OrderList.get(0).getName());
                }
                tblLog.setMessage(updatedBy + " Enter the order number:" + orderNum + " Login confirmation screen");
                logService.insert(tblLog);
            } catch (Exception e) {
                logger.error("添加收费信息转换日志信息失败!", e);
            }

            return Res.ok(pageInfo);
        } catch (Exception e) {
            logger.error("订单查询失败!", e);
        }

        //录入日志信息

        return Res.ok(pageInfo);
    }


    /**
     * 点击确认收费
     * 004410
     *
     * @param type         订单状态
     * @param id           修改的id
     * @param actualAmount 实收金额
     * @param radio        收费方式
     * @param updatedBy    修改人
     * @return 收费确认状态
     */

    @RequestMapping("/successCharge")
    public Res successCharge(String type, Long id, BigDecimal actualAmount, String radio, String updatedBy,String name) {

        TblOrder tblOrder = new TblOrder();
        try {
            tblOrder.setId(id);
            tblOrder.setActualAmount(actualAmount);
            tblOrder.setType(type);
            tblOrder.setPaymentType(radio);
            tblOrder.setUpdatedBy(updatedBy);
            tblOrder.setUpdatedDt(new Date());
            int i = orderService.updateSelectiveByPrimaryKey(tblOrder);

            //录入日志信息
            try {
                TblLog tblLog = new TblLog();
                tblLog.setName(updatedBy);
                tblLog.setDate(new Date());
                tblLog.setPassenger(name);
                tblLog.setMessage(updatedBy + " update " + "id:" + id + " State the order number: " + type + ", Way to charge for " + radio + ", Paid-in amount: " + actualAmount);
                logService.insert(tblLog);
            } catch (Exception e) {
                logger.error("添加收费信息转换日志信息失败!", e);
            }
        } catch (Exception e) {
            logger.error("收费确认信息修改失败!", e);
        }

        return Res.ok();
    }

    /**
     * 点击取消收费
     * 004410
     *
     * @param type      订单状态
     * @param id        修改id
     * @param updatedBy 修改用户名
     * @return 修改状态
     */

    @RequestMapping("/cancelCharge")
    public Res cancelCharge(String type, Long id, String updatedBy,String name) {
        try {
            TblOrder tblOrder = new TblOrder();
            tblOrder.setId(id);
            tblOrder.setType(type);
            tblOrder.setUpdatedBy(updatedBy);
            tblOrder.setUpdatedDt(new Date());
            int i = orderService.updateSelectiveByPrimaryKey(tblOrder);
            TblLog tblLog = new TblLog();
            tblLog.setName(updatedBy);
            tblLog.setDate(new Date());
            tblLog.setPassenger(name);
            tblLog.setMessage(updatedBy + " update " + " id " + id + " State the order number " + type);
            logService.insert(tblLog);
        } catch (Exception e) {
            logger.error("修改取消状态失败!", e);
        }
        return Res.ok();
    }

    /**
     * 点击退款
     * 004410
     *
     * @param type      订单状态
     * @param id        修改id
     * @param updatedBy 修改用户名
     * @return 修改状态
     */

    @RequestMapping("/refundCharge")
    public Res refundCharge(String type, Long id, String updatedBy,String name) {
        TblOrder tblOrder = new TblOrder();
        tblOrder.setId(id);
        tblOrder.setType(type);
        tblOrder.setUpdatedBy(updatedBy);
        tblOrder.setUpdatedDt(new Date());
        int i = orderService.updateSelectiveByPrimaryKey(tblOrder);
        TblLog tblLog = new TblLog();
        tblLog.setName(updatedBy);
        tblLog.setDate(new Date());
        tblLog.setPassenger(name);
        tblLog.setMessage(updatedBy + " update " + " id " + id + " State the order number " + type);
        logService.insert(tblLog);

        Map<String, Object> pageInfo = new HashMap<String, Object>();
        return Res.ok(pageInfo);
    }

    /**
     * ctrlAltH
     * 004410
     *
     * @param request 请求信息
     * @return 修改状态
     */

    @RequestMapping("/ctrlAltH")
    public Res ctrlAltH(HttpServletRequest request) {
        try {
            String ord = request.getParameter("order");
            TblOrder order = JSONObject.parseObject(ord, TblOrder.class);
            TblLog tblLog = new TblLog();
            tblLog.setFid(order.getFid());
            tblLog.setFi(order.getFi());
            tblLog.setFs(order.getFs());
            tblLog.setIp(order.getIp());
            tblLog.setMessage("number: " + order.getCreatedBy() + " ,in: " + new Date() + " ,Operating the:'CTRL+ALT+H'");
            tblLog.setName(order.getCreatedBy());
            tblLog.setDate(new Date());
            tblLog.setPassenger(order.getName());
            logService.insert(tblLog);
        } catch (Exception log) {
            logger.error("请求收费信息中日志信息转换失败!");
        }
        return Res.ok();

    }

}
