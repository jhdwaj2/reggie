package com.lyc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyc.reggie.common.R;
import com.lyc.reggie.dto.DishDto;
import com.lyc.reggie.entity.Category;
import com.lyc.reggie.entity.Dish;
import com.lyc.reggie.entity.DishFlavor;
import com.lyc.reggie.service.CategoryService;
import com.lyc.reggie.service.DishFlavorService;
import com.lyc.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date: 2022/5/22
 * Author: 3378
 * Description:
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {

        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(name !=null, Dish::getName, name);

        wrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (categoryId != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());


        dishDtoPage.setRecords(list);


        return R.success(dishDtoPage);
    }


    @GetMapping("{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        if(dishDto==null){
            return R.error("未查询到改菜品信息");
        }
        return R.success(dishDto);
    }


    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    @PostMapping("/status/0{ids}")
    public R<String> Discontinued(@PathVariable Long ids){

        DishDto dishDto = new DishDto();


        Dish dish = dishService.getById(ids);
        dish.setStatus(0);

//        获取dish
        BeanUtils.copyProperties(dish,dishDto);

//        获取flavor
        List<DishFlavor> flavorList = dishFlavorService.getByDishId(ids);

        dishDto.setFlavors(flavorList);

//        修改状态
        dishService.updateWithFlavor(dishDto);

        return R.success("修改成功");
    }


}
