package com.wxl.gala.service;
import com.wxl.gala.entity.Display;
import java.util.List;

public interface NormalService {
    int insertMessage(String message);
    List<Display> selectAll();
}
