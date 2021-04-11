package com.wxl.gala.controller;

import com.wxl.gala.common.BaseController;
import com.wxl.gala.common.MessageResult;
import com.wxl.gala.entity.User;
import com.wxl.gala.service.LoginService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
    private static Log logger = LogFactory.getLog(NormalController.class);
    @Autowired
    private LoginService loginService;

    @RequestMapping("/log")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户进入登录页面");
        return "login";
    }
    @RequestMapping("/index")
    public String build(HttpServletRequest request, HttpServletResponse response) {
        return "index";
    }
    @RequestMapping("/html/{hm}")
    public String build(@PathVariable String hm, HttpServletRequest request, HttpServletResponse response) {
        return hm;
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public MessageResult login( User user,
                                HttpServletRequest req, HttpServletResponse rsp) {
        MessageResult mr = loginService.login(user, req, rsp);
        return mr;
    }
    @ResponseBody
    @RequestMapping(value = "/checklogin/{token}",method = RequestMethod.GET)
    public MessageResult checklogin(@PathVariable String token,HttpServletRequest req) {
        MessageResult mr = new MessageResult();
        User user = null;
        try {
            user = (User) req.getSession().getAttribute(token);
            if (user != null){
                mr.setCode(200);
                mr.setData(user);
                mr.setSuccess(true);
            }else {
                mr.setCode(-105);
                mr.setData(null);
                mr.setSuccess(false);
            }
        }catch (Exception e){

        }

        return mr;
    }

    @ResponseBody
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public MessageResult register( User user,
                                HttpServletRequest req, HttpServletResponse rsp) {
        MessageResult mr = loginService.register(user,req, rsp);
        return mr;
    }




    @ResponseBody
    @RequestMapping("/test")
    public MessageResult test(HttpServletRequest req, HttpServletResponse rsp, HttpSession se) {
        User user = getLoginfo(req, rsp);
        MessageResult mr = new MessageResult();
        HttpSession session = req.getSession();
        mr.setSuccess(true);
        mr.setCode(200);
        mr.setMessage("测试成功");
        mr.setData("-----ss-----");
        Cookie[] cookies = req.getCookies();
        User us = null;
        if (cookies != null){
            for (Cookie coo : cookies) {
                logger.info("cookie:" + coo.getName() + "--" + coo.getValue() + "--" + coo.getMaxAge() + "--" + coo.getComment());
                if (coo.getName().equals("galaToken")){
                    us = (User)session.getAttribute(coo.getValue());
                    if (us != null){
                        logger.info("session信息:"+us);
                    }
                }
            }
        }
        return mr;
    }



}
