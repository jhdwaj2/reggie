package com.lyc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyc.reggie.entity.Category;
import com.lyc.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
