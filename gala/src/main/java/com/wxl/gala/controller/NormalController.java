package com.wxl.gala.controller;

import com.wxl.gala.common.BaseController;
import com.wxl.gala.common.Pager;
import com.wxl.gala.entity.Display;
import com.wxl.gala.service.NormalService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/normal")
public class NormalController extends BaseController {
    private static Log logger = LogFactory.getLog(NormalController.class);
    @Autowired
    private NormalService normalService;

    @RequestMapping(value = "/test/db", method = RequestMethod.GET)
    @ResponseBody
    public String showMessage(@RequestParam String name) {
        logger.info("-----ss------=======-------ss---");
        try {
            int i = normalService.insertMessage(name);
            if (i > 0){
                logger.info("插入数据成功");
            }else {
                logger.info("插入数据失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return name;
    }
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public Map showGet(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> parameterMap = getParameterMap(req);
        int i = normalService.insertMessage(parameterMap.toString());
        if (i > 0){
            logger.info("插入数据成功");
        }else {
            logger.info("插入数据失败");
        }
        return parameterMap;
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    public String showInfo(@RequestBody String body) {
        int i = normalService.insertMessage(body);
        if (i > 0){
            logger.info("插入数据成功");
        }else {
            logger.info("插入数据失败");
        }
        return body;
    }


//    @RequestMapping(value = "/dis",method = RequestMethod.GET)
//    public ModelAndView dis(HttpServletRequest request,Pager pager) {
//        List<Display> list = normalService.selectAll(pager);
//        logger.info("全部信息:"+list);
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("list",list);
//        mv.addObject("disp",list.get(0));
//        mv.setViewName("alarm.html");
//        return mv;
//    }

    @RequestMapping(value = "/display",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> display(HttpServletRequest request, Pager pager) {

        Map<String, Object> map = normalService.selectAll(pager);

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
