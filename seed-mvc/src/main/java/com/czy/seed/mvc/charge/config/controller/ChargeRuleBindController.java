package com.czy.seed.mvc.charge.config.controller;

import com.czy.seed.mvc.auth.SecurityUser;
import com.czy.seed.mvc.charge.config.entity.TblProductRules;
import com.czy.seed.mvc.util.PrincipalUtil;
import com.czy.seed.mvc.util.Res;

import com.czy.seed.mvc.charge.config.entity.TblLog;
import com.czy.seed.mvc.charge.config.entity.TblProduct;
import com.czy.seed.mvc.charge.config.service.LogService;
import com.czy.seed.mvc.charge.config.service.ProductRulesService;
import com.czy.seed.mvc.charge.config.service.ProductService;
import com.czy.seed.mybatis.base.QueryParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/6/28.
 */
@Controller
@RequestMapping("/charge/rulebind")
public class ChargeRuleBindController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeRuleBindController.class);

    @Autowired
    private ProductRulesService productRulesService;

    @Autowired
    private ProductService productService;

    @Autowired
    private LogService logService;

    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("/charge/rulebind");
    }

    @ResponseBody
    @RequestMapping("/queryList")
    public Res queryList(String pageNum, String pageSize) {
        try {
            int pNum = Integer.valueOf(pageNum);
            int pSize = Integer.valueOf(pageSize);
            QueryParams qp = new QueryParams(TblProductRules.class);
            qp.orderBy("fs").asc().orderBy("productNum").asc();
            QueryParams.Criteria criteria = qp.createCriteria();
            criteria.andEqualTo("status", 1);

            List<TblProductRules> rules = productRulesService.selectPageByParams(pNum, pSize, qp);
            Map<String, Object> rs = new HashMap<>();
            rs.put("rules", rules);

            return Res.ok(rs);
        } catch (Exception e) {
            logger.error("--query ruleList failure!");

            return Res.error();
        }
    }

    @ResponseBody
    @RequestMapping("/queryOne")
    public Res queryOne(String productId) {
        try {
            long pId = Long.valueOf(productId);
            TblProduct product = productService.selectByPrimaryKey(pId);

            Map<String, Object> map = new HashMap<>();
            map.put("product", product);

            return Res.ok(map);
        } catch (Exception e) {
            logger.error("--query rule failure!productId:{}", productId);

            return Res.error();
        }
    }

    @ResponseBody
    @RequestMapping("/queryTotal")
    public Res queryTotal() {
        QueryParams qp = new QueryParams(TblProductRules.class);
        QueryParams.Criteria criteria = qp.createCriteria();
        criteria.andEqualTo("status", 1);
        int total = productRulesService.selectCountByParams(qp);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);

        return Res.ok(map);
    }

    @ResponseBody
    @RequestMapping("/deleteRule")
    public Res deleteRule(String id) {
        try {
            long dId = Integer.valueOf(id);
            TblProductRules productRules = new TblProductRules();
            productRules.setId(dId);
            productRules.setStatus(0);
            productRulesService.updateSelectiveByPrimaryKey(productRules);
            logger.info("--delete rule success.id:{}", id);

            try {
                TblLog log = new TblLog();
                log.setName(PrincipalUtil.getLoginUser().getUsername());
                log.setDate(new Date());
                log.setMessage("工号:" + log.getName() + ",在" + log.getDate() + ",删除了一条规则信息,规则id:" + id);
                logService.insert(log);
            } catch (Exception e) {
                logger.error("+add a delete productRule info error!");
            }

            return Res.ok();
        } catch (Exception e) {
            logger.error("--delete rule failure!id:{}", id);

            return Res.error();
        }

    }

    @ResponseBody
    @RequestMapping("/seleteRule")
    public Res seleteRule(TblProductRules productRules) {
        try {

            QueryParams qp = new QueryParams(TblProductRules.class);
            QueryParams.Criteria criteria = qp.createCriteria();
            criteria.andEqualTo("fs", productRules.getFs());
            criteria.andEqualTo("productNum", productRules.getProductNum());
            criteria.andEqualTo("status",1);
            qp.orderBy("id").desc();
            List<TblProductRules> rules = productRulesService.selectListByParams(qp);
            Map<String, Object> pageInfo = new HashMap<String, Object>();
            pageInfo.put("productRule", rules.size() > 0 ? rules.get(0) : 0);
            return Res.ok(pageInfo);
        } catch (Exception e) {
            logger.error("--查询数据异常!");

            return Res.error();
        }
    }

    @ResponseBody
    @RequestMapping("/addRule")
    public Res addRule(TblProductRules productRules) {

        final SecurityUser loginUser = PrincipalUtil.getLoginUser();

        Date date = new Date();

        productRules.setCreatedBy(loginUser.getUsername());
        productRules.setCreatedDt(date);
        productRules.setUpdatedBy(loginUser.getUsername());
        productRules.setUpdatedDt(date);

        try {
            productRules.setFs(productRules.getFs().toUpperCase());

            productRulesService.insert(productRules);
            logger.info("--add product rule success.");

            try {
                TblLog log = new TblLog();
                log.setName(productRules.getCreatedBy());
                log.setDate(productRules.getCreatedDt());
                log.setMessage("工号:" + log.getName() + ",在" + log.getDate() + ",增加了一条规则信息,规则id:" + productRules.getId()
                        + ",规则名称:" + productRules.getRulesName() + ",规则值:" + productRules.getRulesVal());
                logService.insert(log);
            } catch (Exception e) {
                logger.error("+add a delete productRule info error!");
            }

            return Res.ok();
        } catch (Exception e) {
            logger.error("--add product rule failure!");

            return Res.error();
        }
    }

    @ResponseBody
    @RequestMapping("/updateRule")
    public Res updateRule(TblProductRules productRules) {

        final SecurityUser loginUser = PrincipalUtil.getLoginUser();

        productRules.setUpdatedBy(loginUser.getUsername());
        productRules.setUpdatedDt(new Date());

        try {
            productRules.setFs(productRules.getFs().toUpperCase());

            productRulesService.updateSelectiveByPrimaryKey(productRules);
            logger.info("--update rule success.");

            try {
                TblLog log = new TblLog();
                log.setName(productRules.getUpdatedBy());
                log.setDate(productRules.getUpdatedDt());
                log.setMessage("工号:" + log.getName() + ",在" + log.getDate() + ",修改了一条规则信息,规则id:" + productRules.getId()
                        + ",规则名称:" + productRules.getRulesName() + ",规则值:" + productRules.getRulesVal());
                logService.insert(log);
            } catch (Exception e) {
                logger.error("+add a delete productRule info error!");
            }

            return Res.ok();
        } catch (Exception e) {
            logger.error("--update rule fuilure!");

            return Res.error();
        }
    }
}
