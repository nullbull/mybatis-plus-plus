package com.github.mybatispluspro.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.mybatispluspro.core.Query;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author niuzhenhao
 * @date 2020/9/11 18:07
 * @desc
 */

public interface MultiQueryMapper<T> extends BaseMapper<T> {

     List<Object> selectMulti(@Param("query") Query query);
}
