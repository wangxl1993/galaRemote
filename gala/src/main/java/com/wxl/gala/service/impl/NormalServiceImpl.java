package com.wxl.gala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxl.gala.common.Pager;
import com.wxl.gala.entity.Display;
import com.wxl.gala.mapper.DisplayMapper;
import com.wxl.gala.service.NormalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NormalServiceImpl implements NormalService {
    @Autowired
    private DisplayMapper displayMapper;
    @Override
    public int insertMessage(String message) {
        int i = 0;
        if (message != null){
            Display display = new Display();
            display.setMessage(message);
            display.setCreateTime(new Date());
            i = displayMapper.insert(display);
        }
        return i;
    }

    @Override
    public Map<String,Object> selectAll(Pager pager) {
        Map<String, Object> map = new HashMap<>();
        pager.setPagerParams();
        QueryWrapper<Display> queryWrapper = new QueryWrapper<>();
        List<Display> list = displayMapper.slectByPager(pager);
        Integer count = displayMapper.selectCount(queryWrapper);
        map.put("total",count);
        map.put("rows",list);
        return map;
    }
}
