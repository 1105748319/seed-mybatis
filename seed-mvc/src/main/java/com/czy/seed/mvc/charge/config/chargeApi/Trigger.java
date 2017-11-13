package com.czy.seed.mvc.charge.config.chargeApi;

import com.czy.seed.mvc.charge.config.entity.TblProduct;
import com.czy.seed.mvc.charge.config.entity.TblProductRules;
import com.czy.seed.mvc.charge.config.service.ProductRulesService;
import com.czy.seed.mvc.charge.config.service.ProductService;
import com.czy.seed.mvc.charge.config.util.PropertiesUtil;
import com.czy.seed.mybatis.base.QueryParams;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/7/21.
 */
@Component
@EnableScheduling
public class Trigger {

    @Autowired
    private ProductRulesService productRulesService;

    @Autowired
    private ProductService productService;

    private String PRODUCT = "/charge/api/productList";

    private String PRODUCT_RULES = "/charge/api/productRulesList";

    private static String URL = PropertiesUtil.getStringProperty("synUrl");

    private static String SYN_SWITCH = PropertiesUtil.getStringProperty("synSwitch");

    private static final long INITIAL_DELAY = 2 * 60 * 1000;

    private static final long FIXED_DELAY = 10 * 60 * 1000;

    private static final Logger logger = LoggerFactory.getLogger(Trigger.class);


    /**
     * 启动kis指令
     * liuyun
     */
    @Scheduled(initialDelay = Trigger.INITIAL_DELAY, fixedDelay = Trigger.FIXED_DELAY)
    public void task() {
        logger.info("task run=====================");
        try {
            if ("Y".equals(SYN_SWITCH)) {
                try {
                   synProduct();
                } catch (Exception e) {
                    logger.error("==========同步产品信息异常信息", e);
                }
                try {
                    synProductRules();
                } catch (Exception e) {
                    logger.error("==========同步产品规则信息异常信息", e);
                }
            }

        } catch (Exception e) {
            logger.error("报文 发送报文线程异常", e);
        } finally {
            System.gc();
        }
    }

    /**
     * 004410
     * 同步产品信息
     */

    private void synProduct() {
        Date updateTime = null;
        QueryParams qp = new QueryParams(TblProduct.class);
        qp.orderBy("updatedDt").desc();
        List<TblProduct> productList = productService.selectListByParams(qp);
        //1.判断 productList是否大于0 ,取出第一条 对updateTime赋值
        if (productList != null && productList.size() > 0) {
            updateTime = productList.get(0).getUpdatedDt();
        }
        //2.调用 江东接口 得到返回的list结果
        List<TblProduct> products = httpRequest(updateTime, "product");
        //3.循环LIST 根据ID判断 外站数据库 是否已经存在
        for (TblProduct product : products) {
            if (productService.selectByPrimaryKey(product.getId()) != null) {
                int upNum = productService.updateByPrimaryKey(product);
            } else {
                int addNum = productService.insert(product);
            }
        }
    }

    /**
     * 004410
     * 同步规则信息
     */
    private void synProductRules() {
        Date updateTime = null;
        QueryParams qp = new QueryParams(TblProductRules.class);
        qp.orderBy("updatedDt").desc();
        List<TblProductRules> productRulesList = productRulesService.selectListByParams(qp);
        //1.判断 productList是否大于0 ,取出第一条 对updateTime赋值
        if (productRulesList != null && productRulesList.size() > 0) {
            updateTime = productRulesList.get(0).getUpdatedDt();
        }
        //2.调用 江东接口 得到返回的list结果
        List<TblProductRules> productRules = httpRequest(updateTime, "productRules");
        //3.循环LIST 根据ID判断 外站数据库 是否已经存在
        for (TblProductRules productRule : productRules) {
            if (productRulesService.selectByPrimaryKey(productRule.getId()) != null) {
                int upNum = productRulesService.updateByPrimaryKey(productRule);
            } else {
                int addNum = productRulesService.insert(productRule);
            }
        }

    }
    /**
     * 004410
     * 同步收费规则的接口
     * @return 规则信息
     */
    public List httpRequest(Date updateTime, String data) {
        List list = new ArrayList();
        String smsUrl = "";
        HttpClient httpclient = new DefaultHttpClient();
        if ("product".equals(data)) {
            smsUrl = URL + PRODUCT;
        } else if ("productRules".equals(data)) {
            smsUrl = URL + PRODUCT_RULES;
        }
        HttpPost httppost = new HttpPost(smsUrl);
        String strResult = "";
        String str = null;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (updateTime!=null){
                str = sdf.format(updateTime);
            }

            nameValuePairs.add(new BasicNameValuePair("updateTime", str));
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                    /*读返回数据*/
                String conResult = EntityUtils.toString(response
                        .getEntity());
                if ("product".equals(data)) {
                    list = (List<TblProduct>) com.alibaba.fastjson.JSONObject.parseArray(conResult, TblProduct.class);
                } else if ("productRules".equals(data)) {
                    list = (List<TblProductRules>) com.alibaba.fastjson.JSONObject.parseArray(conResult, TblProductRules.class);
                }

            } else {
                String err = response.getStatusLine().getStatusCode() + "";
                strResult += "发送失败:" + err;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 004410
     * 转换JSON字符串
     */
    private String getStringFromJson(JSONObject adata) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (Object key : adata.keySet()) {
            sb.append("\"" + key + "\":\"" + adata.get(key) + "\",");
        }
        String rtn = sb.toString().substring(0, sb.toString().length() - 1) + "}";
        return rtn;
    }
}
