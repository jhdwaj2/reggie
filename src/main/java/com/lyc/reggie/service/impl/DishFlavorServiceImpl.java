package com.lyc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.reggie.dto.DishDto;
import com.lyc.reggie.entity.DishFlavor;
import com.lyc.reggie.mapper.DishFlavorMapper;
import com.lyc.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date: 2022/5/22
 * Author: 3378
 * Description:
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>implements DishFlavorService {

    @Override
    public List<DishFlavor> getByDishId(Long id) {

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(DishFlavor::getDishId,id);

        return this.list(wrapper);
    }

}
