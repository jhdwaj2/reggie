package com.lyc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyc.reggie.dto.DishDto;
import com.lyc.reggie.entity.Category;
import com.lyc.reggie.entity.Dish;
import com.lyc.reggie.entity.DishFlavor;

import java.util.List;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);


    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);


    void removeWithFlavor(List<Long> ids);
}
