package com.czy.seed.mvc.charge.config.ckiApi;
import com.czy.seed.mvc.util.Res;
import com.czy.seed.mvc.charge.config.entity.TblLog;
import com.czy.seed.mvc.charge.config.service.LogService;
import com.czy.seed.mybatis.base.QueryParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by lenovo on 2017/7/25.
 */
@Controller
@RequestMapping("/cki/Log")
public class LogApi {

    private static final Logger logger = LoggerFactory.getLogger(LogApi.class);

    @Autowired
    private LogService logService;

    private String name;

    @RequestMapping("/select")
    public ModelAndView queryListPerson(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("/charge/logCki");
        name = request.getParameter("name");
        return view;
    }

    @RequestMapping("/logList")
    @ResponseBody
    public Res logList() {
        Res res = Res.ok();
        try {
            QueryParams orderQp = new QueryParams(TblLog.class);
            if (name == null) {
                return Res.error("The operation of the error !");
            }
            orderQp.createCriteria().andEqualTo("passenger",name);
            List<TblLog> logList = logService.selectListByParams(orderQp);//订单列表
            Map<String, Object> pageInfo = new HashMap<String, Object>();
            pageInfo.put("logList", logList);//订单列表

            res = Res.ok(pageInfo);
            name = null;
        } catch (Exception e) {
            res = Res.error("The system call interface:CKI request log");
            logger.error("CKI调用收费页面异常", e);
        }
        return res;
    }
}
