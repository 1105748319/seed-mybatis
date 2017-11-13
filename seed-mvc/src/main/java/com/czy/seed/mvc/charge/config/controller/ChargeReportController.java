package com.czy.seed.mvc.charge.config.controller;

import com.alibaba.fastjson.JSON;
import com.czy.seed.mvc.charge.config.service.OrderService;
import com.czy.seed.mvc.util.Res;
import com.czy.seed.mvc.charge.config.service.ReportService;
import com.czy.seed.mvc.charge.config.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lenovo on 2017/7/12.
 */
@Controller
@RequestMapping("/charge/report")
public class ChargeReportController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeProductController.class);

    @Autowired
    private ReportService reportService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/index")
    public ModelAndView collect () {
        return new ModelAndView("/charge/report");
    }

    @ResponseBody
    @RequestMapping("queryListCollect")
    public Res queryList (HttpServletRequest request) {

        String fi = request.getParameter("fi");
        fi = (fi == null) ? null : fi.trim();
        String createdBy = request.getParameter("createdBy");
        createdBy = (createdBy == null) ? null : createdBy.trim();
        String productType =request.getParameter("productType");
        String group = request.getParameter("group");
        String orderStatus = request.getParameter("orderStatus");


        Map<String, String> params = new HashMap<>();
        params.put("fi", fi);
        params.put("createdBy", createdBy);
        params.put("productType", productType);
        params.put("group", group);
        params.put("orderStatus", orderStatus);

        List<Map<String, Object>> list = reportService.selectCollectGroupNum(params);

        Map<String, Object> map = new HashMap<>();
        map.put("collectData", list);

        return Res.ok(map);
    }

    @RequestMapping("/printCollect")
    public ModelAndView printCollect (HttpServletRequest request, ModelMap model) {
        String fi = request.getParameter("fi");
        fi = (fi == null) ? null : fi.trim();
        String createdBy = request.getParameter("createdBy");
        createdBy = (createdBy == null) ? null : createdBy.trim();
        String productType =request.getParameter("productType");
        String group = request.getParameter("group");
        String orderStatus = request.getParameter("orderStatus");

        Map<String, String> params = new HashMap<>();
        params.put("fi", fi);
        params.put("createdBy", createdBy);
        params.put("productType", productType);
        params.put("group", group);
        params.put("orderStatus", orderStatus);

        List<Map<String, Object>> list = reportService.selectCollectGroupNum(params);

        if ("2".equals(group)) {
            model.put("group", "2");
        } else {
            model.put("group", "1");
        }

        model.put("printData", JSON.toJSON(list));
        return new ModelAndView("/charge/print/printCollect", model);
    }

    @RequestMapping("/exportListCollect")
    public void exportListCollect (HttpServletRequest request, HttpServletResponse response) {

        String fi = request.getParameter("fi");
        fi = (fi == null) ? null : fi.trim();
        String createdBy = request.getParameter("createdBy");
        createdBy = (createdBy == null) ? null : createdBy.trim();
        String productType =request.getParameter("productType");
        String group = request.getParameter("group");
        String orderStatus = request.getParameter("orderStatus");


        Map<String, String> params = new HashMap<>();
        params.put("fi", fi);
        params.put("createdBy", createdBy);
        params.put("productType", productType);
        params.put("group", group);
        params.put("orderStatus", orderStatus);

        List<Map<String, Object>> list = reportService.selectCollectGroupNum(params);

        String fileName = "collectSell.xls";
        response.setHeader("content-type", "application/vnd.ms-excel");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream out = null;
        try {
            out = response.getOutputStream();

        } catch (IOException e) {
            logger.error("--get response.getOutpuStream error!");

        }

        boolean showCreatedby = true;
        if ("2".equals(group)) {
            showCreatedby = false;
        }

        ExcelUtil.downloadExcelCollect(list, out, showCreatedby);

    }

    @ResponseBody
    @RequestMapping("/queryListFlight")
    public Res queryListFlight (HttpServletRequest request) {

        Map<String, Object> result = queryListFlightResult(request);

        return Res.ok(result);
    }

    @RequestMapping("/printFlight")
    public ModelAndView printFlight (HttpServletRequest request, ModelMap model) {
        Map<String, Object> result = queryListFlightResult(request);
        model.put("printData", JSON.toJSON(result));
        return new ModelAndView("/charge/print/printFlight", model);
    }

    @RequestMapping("/exportListFlight")
    public void exportListFlight (HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = queryListFlightResult(request);

        String fileName = "flightSell.xls";
        response.setHeader("content-type", "application/vnd.ms-excel");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream out = null;
        try {
            out = response.getOutputStream();

        } catch (IOException e) {
            logger.error("--get response.getOutpuStream error!");

        }

        try {
            ExcelUtil.downloadExcelFlight(result, out);
        } catch (Exception e){
            logger.error("--export excel flight failure!");
        }

    }

    private Map<String, Object> queryListFlightResult (HttpServletRequest request) {

        Map<String, Object> result = new HashMap<>();
        try {
            String orderNum = request.getParameter("orderNum");
            orderNum = (orderNum == null ? null : orderNum.trim());
            String fiDate = request.getParameter("fiDate");
            fiDate = (fiDate == null ? null : fiDate.trim());
            String status = request.getParameter("status");

            Map<String, Object> params = new HashMap<String, Object>();
            if (orderNum != null) {
                params.put("orderNum", orderNum);
            }
            if (fiDate != null && !"".equals(fiDate)) {
                params.put("fi", fiDate);
            }

            if (status != null) {
                params.put("status", status);
            }

            List<Map<String, Object>> flightInfo = reportService.selectFlightDetail(params);

            List<Map<String, String>> flightSubtotal = new ArrayList<>();
            List<Map<String, String>> flightTotal = new ArrayList<>();
            Map<String, String> mflag = new HashMap<>();
            Map<String, String> tflag = new HashMap<>();


            Integer mIndex = 0, mfstIndex = 0, tIndex = 0, tftIndex = 0, rowNum = 1;
            String oNum = "";

            for (int i = 0; i < flightInfo.size(); i++) {
                Map<String, Object> m = flightInfo.get(i);

                m.put("typeDetail", m.get("productType") + "(" + m.get("unit") + ")");

                if (oNum.equals(m.get("orderNum"))) {
                    rowNum++;
                    flightInfo.get(i - rowNum + 1).put("rowNum", rowNum);
                    m.put("haveType", 0);
                } else {
                    rowNum = 1;
                    m.put("rowNum", rowNum);
                    m.put("haveType", 1);
                }
                oNum = (String) m.get("orderNum");

                Map<String, String> subTotal = new HashMap<>();
                String productType = (String) m.get("productType");
                String paymentType = (String) m.get("paymentType");

                //小计标志
                if ("Y".equals(m.get("status"))) {
//            m.get("paymentType") != null && !"".equals(m.get("paymentType"))) {
                    if (mflag.get(productType + paymentType) == null) {
                        mflag.put(productType + paymentType, productType + paymentType);
                        flightSubtotal.add(subTotal);
                        mfstIndex = mIndex;
                        mIndex = mIndex + 1;
                    } else {
                        for (int x = 0; x < flightSubtotal.size(); x++) {
                            if (mflag.get(productType + paymentType).equals(flightSubtotal.get(x).get("productType") + flightSubtotal.get(x).get("paymentType"))) {
                                mfstIndex = x;
                                break;
                            }
                        }
                    }
                    subTotal.put("productType", productType);
                    subTotal.put("paymentType", paymentType);
                    subTotal.put("totalMoney", "0");
//                if ("Y".equals(m.get("status"))) {
                    m.put("a", "");
                    m.put("mIndex", mfstIndex);
//                }
                }


                //合计标志
                Map<String, String> total = new HashMap<>();
                if ("Y".equals(m.get("status"))) {
                    if (tflag.get(productType) == null) {
                        tflag.put(productType, productType);
                        flightTotal.add(total);
                        tftIndex = tIndex;
                        tIndex = tIndex + 1;
                    } else {
                        for (int x = 0; x < flightTotal.size(); x++) {
                            if (tflag.get(productType).equals(flightTotal.get(x).get("productType"))) {
                                tftIndex = x;
                                break;
                            }
                        }
                    }
                    total.put("productType", productType);
                    total.put("totalMoney", "0");
//                if ("Y".equals(m.get("status"))) {
                    m.put("b", "");
                    m.put("tIndex", tftIndex);
//                }
                }


            }

            //合计
            for (Map<String, Object> m : flightInfo) {
                //小计
                if (m.get("a") != null && m.get("mIndex") != null) {
                    Map<String, String> subMap = flightSubtotal.get((Integer) m.get("mIndex"));
                    subMap.put("totalMoney", String.valueOf(((BigDecimal) (m.get("money"))).longValue() + Long.valueOf(subMap.get("totalMoney"))));
                }

                //合计
                if (m.get("b") != null && m.get("tIndex") != null) {
                    Map<String, String> totalMap = flightTotal.get((Integer) m.get("tIndex"));
                    totalMap.put("totalMoney", String.valueOf(((BigDecimal) (m.get("money"))).longValue() + Long.valueOf(totalMap.get("totalMoney"))));
                }
            }

            //排序，付款方式相同的排在一起
            Comparator<Map<String, String>> com = (p, e) -> {
                if (p.get("paymentType") == null) {
                    return -1;
                }
                if (e.get("paymentType") == null) {
                    return 1;
                }
                int flag = p.get("paymentType").compareTo(e.get("paymentType"));
                return  flag;
            };
            flightSubtotal.sort(com);

            result.put("flightInfo", flightInfo);
            result.put("flightSubtotal", flightSubtotal);
            result.put("flightTotal", flightTotal);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("flightInfo", "");
            result.put("flightSubtotal", "");
            result.put("flightTotal", "");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/queryListPerson")
    public Res queryListPerson (HttpServletRequest request) {

        String orderNum = request.getParameter("orderNum");
        orderNum = (orderNum == null ? "" : orderNum.trim());

        String fi = request.getParameter("fi");
        fi = (fi == null ? "" : fi.trim());

        String name = request.getParameter("name");
        name = (name == null ? "" : name.trim());

        String licenseNo = request.getParameter("licenseNo");
        licenseNo = (licenseNo == null ? "" : licenseNo.trim());

        String type = request.getParameter("type");

        String createdBy = request.getParameter("createdBy");
        createdBy = (createdBy == null ? "" : createdBy.trim());


        Map<String, Object> params = new HashMap<>();

        if ("".equals(orderNum) && "".equals(fi) && "".equals(name)
                && "".equals(licenseNo) && "".equals(createdBy)) {
            Calendar calendar = Calendar.getInstance();
            Date endTime = calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH, -3);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startTime = calendar.getTime();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            params.put("startTime", sdf.format(startTime));
            params.put("endTime", sdf.format(endTime));
        }
        params.put("orderNum",orderNum);
        params.put("fi",fi);
        params.put("name",name);
        params.put("licenseNo",licenseNo);
        params.put("type",type);
        params.put("createdBy",createdBy);

        List<Map<String, Object>> list = reportService.selectPersonDetail(params);

        Map<String, Object> map = new HashMap<>();
        map.put("personData", list);

        return Res.ok(map);
    }


    @RequestMapping("/printPerson")
    public ModelAndView printPerson (HttpServletRequest request, ModelMap model) {
        String orderNum = request.getParameter("orderNum");
        orderNum = (orderNum == null ? "" : orderNum.trim());

        String fi = request.getParameter("fi");
        fi = (fi == null ? "" : fi.trim());

        String name = request.getParameter("name");
        name = (name == null ? "" : name.trim());

        String licenseNo = request.getParameter("licenseNo");
        licenseNo = (licenseNo == null ? "" : licenseNo.trim());

        String type = request.getParameter("type");

        String createdBy = request.getParameter("createdBy");
        createdBy = (createdBy == null ? "" : createdBy.trim());


        Map<String, Object> params = new HashMap<>();

        if ("".equals(orderNum) && "".equals(fi) && "".equals(name)
                && "".equals(licenseNo) && "".equals(createdBy)) {
            Calendar calendar = Calendar.getInstance();
            Date endTime = calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH, -3);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
            Date startTime = calendar.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            params.put("startTime", sdf.format(startTime));
            params.put("endTime", sdf.format(endTime));
        }
        params.put("orderNum",orderNum);
        params.put("fi",fi);
        params.put("name",name);
        params.put("licenseNo",licenseNo);
        params.put("type",type);
        params.put("createdBy",createdBy);

        List<Map<String, Object>> list = reportService.selectPersonDetail(params);

        model.put("printData", JSON.toJSON(list));
        return new ModelAndView("/charge/print/printPerson", model);
    }


    @ResponseBody
    @RequestMapping("/deleteOnePerson")
    public Res deleteOnePerson (String id) {

        Long oId = Long.valueOf(id);
        try {
            orderService.deleteByPrimaryKey(oId);

            logger.info("deleteOnePerson success.id:{}",oId);
            return Res.ok();
        } catch (Exception e) {
            logger.error("deleteOnePerson failure.id:{}",oId);

            return Res.error();
        }
    }

    @RequestMapping("/exportListPerson")
    public void exportListPerson (HttpServletRequest request, HttpServletResponse response) {

        String orderNum = request.getParameter("orderNum");
        orderNum = (orderNum == null ? "" : orderNum.trim());

        String fi = request.getParameter("fi");
        fi = (fi == null ? "" : fi.trim());

        String name = request.getParameter("name");
        name = (name == null ? "" : name.trim());

        String licenseNo = request.getParameter("licenseNo");
        licenseNo = (licenseNo == null ? "" : licenseNo.trim());

        String type = request.getParameter("type");

        String createdBy = request.getParameter("createdBy");
        createdBy = (createdBy == null ? "" : createdBy.trim());


        Map<String, Object> params = new HashMap<>();

        if ("".equals(orderNum) && "".equals(fi) && "".equals(name)
                && "".equals(licenseNo) && "".equals(createdBy)) {
            Calendar calendar = Calendar.getInstance();
            Date endTime = calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH, -3);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
            Date startTime = calendar.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            params.put("startTime", sdf.format(startTime));
            params.put("endTime", sdf.format(endTime));
        }
        params.put("orderNum",orderNum);
        params.put("fi",fi);
        params.put("name",name);
        params.put("licenseNo",licenseNo);
        params.put("type",type);
        params.put("createdBy",createdBy);

        List<Map<String, Object>> list = reportService.selectPersonDetail(params);

        String fileName = "personSell.xls";
        response.setHeader("content-type", "application/vnd.ms-excel");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream out = null;
        try {
            out = response.getOutputStream();

        } catch (IOException e) {
            logger.error("--get response.getOutpuStream error!");

        }
        ExcelUtil.downloadExcelPerson(list, out);

    }

}
