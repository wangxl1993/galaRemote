package com.wxl.gala.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxl.gala.common.MessageResult;
import com.wxl.gala.entity.User;
import com.wxl.gala.mapper.UserMapper;
import com.wxl.gala.service.LoginService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {
    private static Log logger = LogFactory.getLog(LoginServiceImpl.class);
    @Autowired
    private UserMapper userMapper;
    @Override
    public MessageResult login(String username, String password,
                               HttpServletRequest req, HttpServletResponse rsp) {
        MessageResult mr = new MessageResult();
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(User::getUsername,username).eq(User::getPassword,password);
        List<User> users = userMapper.selectList(queryWrapper);
        String token = "";
        if (users != null && users.size()==1){
            token = "gala"+ UUID.randomUUID().toString();
            mr.setSuccess(true);
            mr.setCode(200);
            mr.setMessage("登录成功");
            mr.setData(token);
            HttpSession session = req.getSession();
            session.setAttribute(token,users.get(0));
            session.setMaxInactiveInterval(3*60);  //10分钟
            Cookie cookie = new Cookie("galaToken",token);
            rsp.addCookie(cookie);
        }else {
            mr.setSuccess(false);
            mr.setCode(-103);
            mr.setMessage("用户名或密码错误");
            mr.setData(null);
        }
        logger.info("登录流程"+token);
        return mr;
    }
}
