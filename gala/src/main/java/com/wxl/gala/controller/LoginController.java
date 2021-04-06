package com.wxl.gala.controller;

import com.wxl.gala.common.BaseController;
import com.wxl.gala.common.MessageResult;
import com.wxl.gala.entity.User;
import com.wxl.gala.service.LoginService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping("/index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户进入首页面");
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public MessageResult login( String username, String password,
                                HttpServletRequest req, HttpServletResponse rsp) {
        MessageResult mr = loginService.login(username, password, req, rsp);
        return mr;
    }

    @ResponseBody
    @RequestMapping("/test")
    public MessageResult test(HttpServletRequest req, HttpServletResponse rsp, HttpSession se) {
        User user = getLoginfo(req, rsp);
        MessageResult mr = new MessageResult();
        HttpSession session = req.getSession();
        Object jsessionid = session.getAttribute("JSESSIONID");
        Object gt = session.getAttribute("cookie-token");
        Object st = session.getAttribute("session-token");
        mr.setSuccess(true);
        mr.setCode(200);
        mr.setMessage("登录成功");
        mr.setData("-----ss-----");

        Cookie[] cookies = req.getCookies();
        for (Cookie coo : cookies) {
            logger.info("cookie:" + coo.getName() + "--" + coo.getValue() + "--" + coo.getMaxAge() + "--" + coo.getComment());
        }
        logger.info("jsessionid:" + jsessionid);
        logger.info(st + ":cookie-token:" + gt);
        logger.info(se.getAttribute("JSESSIONID") + "--" + se.getAttribute("cookie-token") +
                "--" + se.getAttribute("session-token"));
        return mr;
    }

    @RequestMapping("/build")
    public String build(HttpServletRequest request, HttpServletResponse response) {
        return "build";
    }

}
