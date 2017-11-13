package com.czy.seed.mvc.charge.config.chargeApi.client;

import com.czy.seed.mvc.charge.config.entity.TblProductRules;
import com.czy.seed.mvc.charge.config.service.ProductRulesService;
import com.czy.seed.mvc.charge.config.service.ProductService;
import com.czy.seed.mvc.charge.config.entity.TblProduct;

import com.czy.seed.mybatis.base.QueryParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;

import java.util.List;


/**
 * Created by Administrator on 2017/7/21.
 */



@Controller
@RequestMapping("/charge/api")
public class ProductRulesClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductRulesClient.class);

    @Autowired
    private ProductRulesService productRulesService;

    @Autowired
    private ProductService productService;


    /**
     * 产品信息规则接口
     * 004410
     * @param
     * @return 查到的产品信息
     */
    @ResponseBody
    @RequestMapping("/productList")
    public  List<TblProduct>  productList (HttpServletRequest request) {
        String updateTime = "".equals(request.getParameter("updateTime"))?null:request.getParameter("updateTime");
//        Object succesResponse = JSON.parse(request.getParameter("msg"));    //先转换成Object
//        Map map = (Map)succesResponse;
//        map.get("time").toString();
        QueryParams qp = new QueryParams(TblProductRules.class);
        QueryParams.Criteria criteria = qp.createCriteria();
        criteria.andGreaterThan("updatedDt",updateTime);
        List<TblProduct>  TblProductList= productService.selectListByParams(qp);
        return TblProductList;
    }


    /**
     * 产品规则信息接口
     * 004410
     * @param request
     * @return 查到的规则信息
     */
    @ResponseBody
    @RequestMapping("/productRulesList")
    public List<TblProductRules> productRulesList (HttpServletRequest request) {
        String updateTime = "".equals(request.getParameter("updateTime"))?null:request.getParameter("updateTime");
        QueryParams qp = new QueryParams(TblProductRules.class);
        QueryParams.Criteria criteria = qp.createCriteria();
        criteria.andGreaterThan("updatedDt",updateTime);
        List<TblProductRules>  TblProductRulesList= productRulesService.selectListByParams(qp);
        return TblProductRulesList;
    }

}
