package com.wxl.gala.controller;

import com.wxl.gala.common.BaseController;
import com.wxl.gala.common.Pager;
import com.wxl.gala.service.DataService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/data")
public class DataController extends BaseController {
    private static Log logger = LogFactory.getLog(DataController.class);
    @Autowired
    private DataService dataService;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public Map showGet(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> parameterMap = getParameterMap(req);
        int i = dataService.insertMessage(parameterMap.toString());
        if (i > 0){
            logger.info("插入数据成功");
        }else {
            logger.info("插入数据失败");
        }
        return parameterMap;
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    public String showInfo(@RequestBody String body,HttpServletRequest req) {
        String data = "";
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (parameterMap != null){
            data += parameterMap.keySet();
            data += parameterMap.values();
        }
        logger.info(req.getParameter("namemy")+"---post:"+data);
        logger.info("---postBody:"+data);
        int i = dataService.insertMessage(body);
        if (i > 0){
            logger.info("插入数据成功");
        }else {
            logger.info("插入数据失败");
        }
        return body;
    }


    @RequestMapping(value = "/display",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> display(HttpServletRequest request, Pager pager) {

        Map<String, Object> map = dataService.selectAll(pager);

        return map;
    }

    protected Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, Object> requestMap = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    requestMap.put(paramName, paramValue);
                }
            }
        }
        return requestMap;
    }
}
