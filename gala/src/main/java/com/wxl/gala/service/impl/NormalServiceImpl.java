package com.wxl.gala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxl.gala.entity.Display;
import com.wxl.gala.mapper.DisplayMapper;
import com.wxl.gala.service.NormalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    public List<Display> selectAll() {
        QueryWrapper<Display> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Display::getCreateTime);
        List<Display> list = displayMapper.selectList(queryWrapper);
        return list;
    }
}
