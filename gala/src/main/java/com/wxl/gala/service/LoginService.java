package com.wxl.gala.service;

import com.wxl.gala.common.MessageResult;
import com.wxl.gala.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    MessageResult login(User user, HttpServletRequest req, HttpServletResponse rsp);
    MessageResult register(User user, HttpServletRequest req, HttpServletResponse rsp);
}
