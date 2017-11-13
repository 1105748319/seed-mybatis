package com.czy.seed.mvc.charge.config.ckiApi;

import com.czy.seed.mvc.charge.config.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lenovo on 2017/7/25.
 */
@Controller
@RequestMapping("/cki/report")
public class ReportApi {

    private static final Logger logger = LoggerFactory.getLogger(ReportApi.class);

    @Autowired
    private ReportService reportService;

    @RequestMapping("/person")
    public ModelAndView queryListPerson (HttpServletRequest request, ModelMap modelMap) {

        String showPage = request.getParameter("showPage");
        String name = request.getParameter("name");
        if (showPage == null) {
            showPage = "";
        }
        if (name == null || "".equals(name)) {
            name = "cki";
        }

        modelMap.put("showPage", showPage);
        modelMap.put("name", name);

        return new ModelAndView("/charge/report", modelMap);
    }
}
