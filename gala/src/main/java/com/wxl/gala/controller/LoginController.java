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


}
