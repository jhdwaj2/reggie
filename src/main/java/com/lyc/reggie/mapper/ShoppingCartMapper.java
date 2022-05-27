package com.lyc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyc.reggie.entity.Dish;
import com.lyc.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
