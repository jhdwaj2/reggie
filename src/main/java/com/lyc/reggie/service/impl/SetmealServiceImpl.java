package com.lyc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.reggie.common.CustomException;
import com.lyc.reggie.dto.SetmealDto;
import com.lyc.reggie.entity.Category;
import com.lyc.reggie.entity.Setmeal;
import com.lyc.reggie.entity.SetmealDish;
import com.lyc.reggie.mapper.CategoryMapper;
import com.lyc.reggie.mapper.SetmealMapper;
import com.lyc.reggie.service.CategoryService;
import com.lyc.reggie.service.SetmealDishService;
import com.lyc.reggie.service.SetmealService;
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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithSetmealDish(SetmealDto setmealDto) {
//        存入setmeal
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        Long id = setmealDto.getId();

        setmealDishes=setmealDishes.stream().peek((item)-> item.setSetmealId(id)).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        wrapper.eq(Setmeal::getStatus,1);

        int count = this.count(wrapper);

        if(count>0){
            throw new CustomException("套餐正在售卖中,不能删除");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(queryWrapper);

    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmeal.getId());

        List<SetmealDish> list = setmealDishService.list(wrapper);

        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());

        setmealDishService.remove(wrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes=setmealDishes.stream().peek((item)->item.setSetmealId(setmealDto.getId())).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }
}
