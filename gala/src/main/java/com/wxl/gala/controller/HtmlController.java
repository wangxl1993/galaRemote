package com.wxl.gala.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HtmlController {
    private static Log logger = LogFactory.getLog(HtmlController.class);

    @RequestMapping("/html/alarm")
    public String build( HttpServletRequest request, HttpServletResponse response) {
        return "alarm";
    }








}
