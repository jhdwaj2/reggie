package com.lyc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyc.reggie.dto.SetmealDto;
import com.lyc.reggie.entity.Employee;
import com.lyc.reggie.entity.Setmeal;

import java.util.List;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
public interface SetmealService extends IService<Setmeal> {
    void saveWithSetmealDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);

    SetmealDto getByIdWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);
}

