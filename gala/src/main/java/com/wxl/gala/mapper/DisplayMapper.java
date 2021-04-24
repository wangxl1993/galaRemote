package com.wxl.gala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxl.gala.common.Pager;
import com.wxl.gala.entity.Display;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DisplayMapper extends BaseMapper<Display> {

    List<Display> slectByPager(Pager pager);

}