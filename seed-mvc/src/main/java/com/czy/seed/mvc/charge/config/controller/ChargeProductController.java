package com.czy.seed.mvc.charge.config.controller;

import com.czy.seed.mvc.auth.SecurityUser;
import com.czy.seed.mvc.base.controller.BaseController;
import com.czy.seed.mvc.charge.config.entity.TblLog;
import com.czy.seed.mvc.charge.config.entity.TblProduct;
import com.czy.seed.mvc.charge.config.service.LogService;
import com.czy.seed.mvc.charge.config.service.ProductRulesService;
import com.czy.seed.mvc.charge.config.service.ProductService;
import com.czy.seed.mvc.charge.config.util.ChargeMap;
import com.czy.seed.mvc.charge.config.util.PropertiesUtil;
import com.czy.seed.mvc.util.PrincipalUtil;
import com.czy.seed.mvc.util.Res;
import com.czy.seed.mybatis.base.QueryParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by lenovo on 2017/6/26.
 */
@Controller
@RequestMapping("/charge/product")
public class ChargeProductController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private LogService logService;

    @Autowired
    private ProductRulesService productRulesService;

    @RequestMapping("/index")
    public ModelAndView index() {

        return new ModelAndView("/charge/product");
    }


    @ResponseBody
    @RequestMapping("/queryTotal")
    public Res queryTotal(TblProduct tblProduct) {
        QueryParams qp = new QueryParams(TblProduct.class);
        QueryParams.Criteria criteria = qp.createCriteria();
        if (tblProduct.getProductNum() != null && !"".equals(tblProduct.getProductNum())) {
            criteria.andEqualTo("productNum", tblProduct.getProductNum());
        }

        if (tblProduct.getProductName() != null && !"".equals(tblProduct.getProductName())) {
            criteria.andLike("productName", "%" + tblProduct.getProductName() + "%");
        }

        if (tblProduct.getProductType() != null && !"".equals(tblProduct.getProductType())) {
            criteria.andEqualTo("productType", tblProduct.getProductType());
        }

        criteria.andEqualTo("status", 1);

        try {
            int total = productService.selectCountByParams(qp);

            Map<String, Object> map = new HashMap<>();
            map.put("total", total);

            return Res.ok(map);
        } catch (Exception e) {
            logger.error("--query product total failure!");

            return Res.error();
        }
    }

    @ResponseBody
    @RequestMapping("/queryList")
    public Res queryList(TblProduct tblProduct, String pageSize, String pageNum) {
        try {
            int pSize = 1;
            int pNum = 1;
            pSize = Integer.valueOf(pageSize);
            pNum = Integer.valueOf(pageNum);
            List<TblProduct> products = productService.selectList(tblProduct, pSize, pNum);

            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("pageInfo", products);

            return Res.ok(pageInfo);
        } catch (Exception e) {
            logger.error("--query productList failure!");

            return Res.error();
        }


    }

    @ResponseBody
    @RequestMapping("/queryProductType")
    public Res queryProductType() {

        List<Map<String, Object>> types = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();

        map.put("label", "ALL");
        map.put("value", "");
        types.add(map);

        map = new HashMap<>();
        map.put("label", PropertiesUtil.getStringProperty("baggage"));
        map.put("value", "BAGGAGE");
        types.add(map);

        map = new HashMap<>();
        map.put("label", PropertiesUtil.getStringProperty("seat"));
        map.put("value", "SEAT");
        types.add(map);

        map = new HashMap<>();
        map.put("label", PropertiesUtil.getStringProperty("baggage_others"));
        map.put("value", "BAGGAGE(OTHERS)");
        types.add(map);

        map = new HashMap<>();
        map.put("label", PropertiesUtil.getStringProperty("other"));
        map.put("value", "OTHERS");
        types.add(map);


        return Res.ok(types);

    }


    @ResponseBody
    @RequestMapping("/addOneProduct")
    public Res addOneProduct(TblProduct product) {

        QueryParams qp = new QueryParams(TblProduct.class);
        QueryParams.Criteria criteria = qp.createCriteria();
        criteria.andEqualTo("productNum", product.getProductNum());
        criteria.andEqualTo("status", 1);

        List<TblProduct> isHave = productService.selectListByParams(qp);
        if (isHave != null && isHave.size() > 0) {
            return Res.error("1");
        }

        final SecurityUser loginUser = PrincipalUtil.getLoginUser();

        Date date = new Date();
        product.setCreatedDt(date);
        product.setCreatedBy(loginUser.getUsername());
        product.setUpdatedDt(date);
        product.setUpdatedBy(loginUser.getUsername());
        try {
            productService.insert(product);
            logger.info("--insert product success.id:{}", product.getId());

            try{
                TblLog log = new TblLog();
                log.setName(product.getCreatedBy());
                log.setDate(product.getCreatedDt());
                log.setMessage("工号:" + product.getCreatedBy() + ",在" +
                        product.getCreatedDt() + ",增加了一条产品信息,产品id:" + product.getId() + "产品类型" +
                        product.getProductType() + ",产品名称:" + product.getProductName());

                logService.insert(log);
            } catch (Exception e) {
                logger.error("+add product info logger error!");
            }

            return Res.ok();
        } catch (Exception e) {
            logger.error("--insert product failure!");

            return Res.error();
        }
    }

    @ResponseBody
    @RequestMapping("/deleteProduct")
    public Res deleteProduct(String id) {
        if (id == null) {
            return Res.error();
        }

        try {
            TblProduct product = new TblProduct();
            product.setId(Long.valueOf(id));
            product.setStatus(0);
            productService.updateSelectiveByPrimaryKey(product);
            logger.info("--delete product success by id.id:{}", id);

            //删除对应的产品规则
            productRulesService.updateProductRulesStatus(Integer.valueOf(id));

            try{
                TblLog log = new TblLog();
                log.setName(PrincipalUtil.getLoginUser().getUsername());
                log.setDate(new Date());
                log.setMessage("工号:" + log.getName() + ",在" +
                        log.getDate() + ",删除了一条产品信息,产品id:" + id);

                logService.insert(log);
            } catch (Exception e) {
                logger.error("+add a delete product info logger error!");
            }

            return Res.ok();
        } catch (Exception e) {
            logger.error("--delete product failure by productNum.productNum:{}", id);

            return Res.error();
        }
    }

    @ResponseBody
    @RequestMapping("/updateProduct")
    public Res updateProduct(TblProduct product) {
        if (product.getId() == null) {
            return Res.error();
        }

        QueryParams qp = new QueryParams(TblProduct.class);
        QueryParams.Criteria criteria = qp.createCriteria();
        criteria.andEqualTo("status", 1);
        criteria.andEqualTo("productNum", product.getProductNum());
        criteria.andNotEqualTo("id", product.getId());
        List<TblProduct> list = productService.selectListByParams(qp);
        if (list != null && list.size() > 0) {
            return Res.error("1");
        }


        final SecurityUser loginUser = PrincipalUtil.getLoginUser();

        product.setUpdatedDt(new Date());
        product.setUpdatedBy(loginUser.getUsername());

        try {
            productService.updateSelectiveByPrimaryKey(product);
            logger.info("--update product success.id:{}", product.getId());

            try{
                TblLog log = new TblLog();
                log.setName(product.getUpdatedBy());
                log.setDate(product.getUpdatedDt());
                log.setMessage("工号:" + product.getUpdatedBy() + ",在" +
                        product.getUpdatedDt() + ",修改了一条产品信息,产品id:" + product.getId() + "产品类型" +
                        product.getProductType() + ",产品名称:" + product.getProductName());

                logService.insert(log);
            } catch (Exception e) {
                logger.error("+add a updae product info logger error!");
            }

            return Res.ok();
        } catch (Exception e) {
            logger.error("--update product failure!id:{}", product.getId());

            return Res.error();
        }
    }


}
