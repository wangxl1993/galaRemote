package com.wxl.gala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxl.gala.entity.Display;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DisplayMapper extends BaseMapper<Display> {
    int deleteByPrimaryKey(Integer id);

    int insert(Display record);

    int insertSelective(Display record);

    Display selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Display record);

    int updateByPrimaryKeyWithBLOBs(Display record);
}