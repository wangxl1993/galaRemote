package com.wxl.gala.common;

import com.wxl.gala.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {
    private static Log logger = LogFactory.getLog(BaseController.class);

    public User getLoginfo(HttpServletRequest req, HttpServletResponse rsp){
        Cookie[] cookies = req.getCookies();
        HttpSession session = req.getSession();
        if (session!=null && cookies!=null && cookies.length>0){
            for (Cookie coo: cookies){
                if (Constant.Token.equals(coo.getName())){
                    String token = coo.getValue();
                    User user = (User) session.getAttribute(token);
                    logger.info("用户登录信息:"+user);
                    return user;
                }
            }
        }
        return null;
    }




}
