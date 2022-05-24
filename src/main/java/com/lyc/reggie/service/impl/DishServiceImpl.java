package com.lyc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.reggie.dto.DishDto;
import com.lyc.reggie.entity.Dish;
import com.lyc.reggie.entity.DishFlavor;
import com.lyc.reggie.entity.Employee;
import com.lyc.reggie.mapper.DishMapper;
import com.lyc.reggie.mapper.EmployeeMapper;
import com.lyc.reggie.service.DishFlavorService;
import com.lyc.reggie.service.DishService;
import com.lyc.reggie.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long id = dishDto.getId();

        List<DishFlavor> flavors=dishDto.getFlavors();
        flavors=flavors.stream().peek((item)-> item.setDishId(id)).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dish.getId());

        List<DishFlavor> flavors = dishFlavorService.list(wrapper);

        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(wrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().peek((item) -> item.setDishId(dishDto.getId())).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void removeWithFlavor(List<Long> ids) {

        this.removeByIds(ids);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();

        wrapper.in(DishFlavor::getDishId,ids);

        dishFlavorService.remove(wrapper);
    }
}
