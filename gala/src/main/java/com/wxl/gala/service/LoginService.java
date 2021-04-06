package com.wxl.gala.service;

import com.wxl.gala.common.MessageResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    MessageResult login(String username, String password, HttpServletRequest req, HttpServletResponse rsp);
}
