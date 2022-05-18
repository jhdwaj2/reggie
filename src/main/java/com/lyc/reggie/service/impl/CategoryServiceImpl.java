package com.lyc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.reggie.common.CustomException;
import com.lyc.reggie.entity.Category;
import com.lyc.reggie.entity.Dish;
import com.lyc.reggie.entity.Employee;
import com.lyc.reggie.entity.Setmeal;
import com.lyc.reggie.mapper.CategoryMapper;
import com.lyc.reggie.mapper.EmployeeMapper;
import com.lyc.reggie.service.CategoryService;
import com.lyc.reggie.service.DishService;
import com.lyc.reggie.service.EmployeeService;
import com.lyc.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Dish> wrapper1 = new LambdaQueryWrapper<>();

        wrapper1.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(wrapper1);

        if(count1>0){
            throw new CustomException("当前分类关联了菜品,不能直接删除");
        }

        LambdaQueryWrapper<Setmeal> wrapper2 = new LambdaQueryWrapper<>();

        wrapper2.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(wrapper2);

        if(count2>0){
            throw new CustomException("当前分类关联了套餐,不能直接删除");
        }

        super.removeById(id);
    }
}
