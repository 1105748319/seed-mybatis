package com.czy.seed.mvc.sys.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by PLC on 2017/5/21.
 */

@Controller
@RequestMapping("/sys/login")
public class SysLoginController {

    @RequestMapping(value = "/login1",method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String login(String username, String password, HttpSession session) {
        return "success";
    }

    public String logout() {
        return "false";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @PreAuthorize("authenticated and hasPermission('hello', 'view')")
    @ResponseBody
    public String hello(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("message", username);
        return "hello";
    }

//    @PreAuthorize("authenticated and hasPermission('hello', 'view')"): 表示只有当前已登录的并且拥有("hello", "view")权限的用户才能访问此页面
//    SecurityContextHolder.getContext().getAuthentication().getName(): 获取当前登录的用户，也可以通过HttpServletRequest.getRemoteUser()获取

}
