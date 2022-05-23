package com.lyc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyc.reggie.dto.DishDto;
import com.lyc.reggie.entity.DishFlavor;

import java.util.List;

/**
 * Date: 2022/5/22
 * Author: 3378
 * Description:
 */
public interface DishFlavorService extends IService<DishFlavor> {

    List<DishFlavor> getByDishId(Long id);

}

