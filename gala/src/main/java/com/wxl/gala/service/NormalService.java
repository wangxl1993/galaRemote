package com.wxl.gala.service;
import com.wxl.gala.common.Pager;
import com.wxl.gala.entity.Display;
import java.util.List;
import java.util.Map;

public interface NormalService {
    int insertMessage(String message);
    Map<String,Object> selectAll(Pager pager);
}
