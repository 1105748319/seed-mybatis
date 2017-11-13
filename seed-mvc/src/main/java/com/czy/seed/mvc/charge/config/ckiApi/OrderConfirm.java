package com.czy.seed.mvc.charge.config.ckiApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2017/7/26.
 */
@Controller
@RequestMapping("/cki/orderConfirm")
public class OrderConfirm {

    private static final Logger logger = LoggerFactory.getLogger(OrderConfirm.class);

    @RequestMapping("/orderConfirmation")
    public ModelAndView orderConfirmation(String name) {
        ModelAndView view = new ModelAndView("/charge/orderConfirmation");
        if (name == null) {
            logger.error("收费确认接口调用操作人信息为空");
        }
        view.addObject("name", name);
        view.addObject("CKI", "CKI");
        return view;
    }


}
