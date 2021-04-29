package com.wxl.gala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxl.gala.common.Pager;
import com.wxl.gala.entity.Display;
import com.wxl.gala.mapper.DisplayMapper;
import com.wxl.gala.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataServiceImpl implements DataService {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private DisplayMapper displayMapper;

    @Override
    public int insertMessage(String message) {
        int i = 0;
        if (message != null) {
            Display display = new Display();
            display.setMessage(message);
            display.setCreateTime(new Date());
            i = displayMapper.insert(display);
        }
        return i;
    }

    @Override
    public Map<String, Object> selectAll(Pager pager) {
        Map<String, Object> map = new HashMap<>();
        pager.setPagerParams();
        QueryWrapper<Display> queryWrapper = new QueryWrapper<>();
        List<Display> list = displayMapper.slectByPager(pager);
        Integer count = displayMapper.selectCount(queryWrapper);
        if (list != null && list.size() > 0) {
            for (Display dis : list) {
                if (dis.getCreateTime() != null) {
                    String time = sdf.format(dis.getCreateTime());
                    dis.setFormatCreateTime(time);
                }
            }
        }

        map.put("total", count);
        map.put("rows", list);
        return map;
    }
}
