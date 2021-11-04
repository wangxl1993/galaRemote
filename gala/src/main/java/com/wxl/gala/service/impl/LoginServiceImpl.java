package com.wxl.gala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxl.gala.common.MessageResult;
import com.wxl.gala.entity.User;
import com.wxl.gala.mapper.UserMapper;
import com.wxl.gala.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {
    private static Log logger = LogFactory.getLog(LoginServiceImpl.class);
    @Autowired
    private UserMapper userMapper;

    @Override
    public MessageResult login(User user,
                               HttpServletRequest req, HttpServletResponse rsp) {
        MessageResult mr = new MessageResult();
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            mr.setSuccess(false);
            mr.setCode(-104);
            mr.setMessage("数据非法");
            mr.setData(null);
            return mr;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().
                eq(User::getEmail, user.getUsername()).eq(User::getPassword, user.getPassword())
                .or().eq(User::getPhone, user.getUsername()).eq(User::getPassword, user.getPassword());
        List<User> users = userMapper.selectList(queryWrapper);
        logger.info("用户信息:" + users);
        String token = "";
        if (users != null && users.size() == 1) {
            token = "gala" + UUID.randomUUID().toString();
            mr.setSuccess(true);
            mr.setCode(200);
            mr.setMessage("登录成功");
            mr.setData(token);
            HttpSession session = req.getSession();
            User sessionUser = users.get(0);
            sessionUser.setPassword("");
            session.setAttribute(token, sessionUser);
            //10分钟
            session.setMaxInactiveInterval(2 * 60);
            Cookie cookie = new Cookie("galaToken", token);
            cookie.setMaxAge(60*20);
            cookie.setPath("/");
            rsp.addCookie(cookie);
        } else {
            mr.setSuccess(false);
            mr.setCode(-103);
            mr.setMessage("用户名或密码错误");
            mr.setData(null);
        }
        logger.info("登录流程" + token);
        return mr;
    }

    @Override
    public MessageResult register(User user,
                                  HttpServletRequest req, HttpServletResponse rsp) {
        MessageResult mr = new MessageResult();
        if (user == null || StringUtils.isEmpty(user.getUsername()) ||
                StringUtils.isEmpty(user.getPassword()) || StringUtils.isEmpty(user.getEmail())
                || StringUtils.isEmpty(user.getPhone())) {
            mr.setSuccess(false);
            mr.setCode(-104);
            mr.setMessage("数据非法");
            mr.setData(null);
            return mr;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(User::getEmail, user.getEmail()).or().eq(User::getPhone, user.getPhone());
        List<User> users = userMapper.selectList(queryWrapper);
        if (users != null && users.size() > 0) {
            mr.setSuccess(false);
            mr.setCode(-203);
            mr.setMessage("手机号或邮箱已注册");
            mr.setData(null);
        } else {
//            password = DigestUtils.md5DigestAsHex(password.getBytes());
            Date date = new Date();
            User us = new User();
            us.setUsername(user.getUsername());
            us.setEmail(user.getEmail());
            us.setPassword(user.getPassword());
            us.setCreated(date);
            us.setPhone(user.getPhone());
            us.setUpdated(date);
            userMapper.insert(us);
            mr.setSuccess(true);
            mr.setCode(200);
            mr.setMessage("注册成功，请登录");
            mr.setData(null);
        }
        return mr;
    }
}
