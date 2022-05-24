package com.lyc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyc.reggie.common.R;
import com.lyc.reggie.dto.DishDto;
import com.lyc.reggie.entity.Category;
import com.lyc.reggie.entity.Dish;
import com.lyc.reggie.service.CategoryService;
import com.lyc.reggie.service.DishFlavorService;
import com.lyc.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/status/0")
    public R<String> discontinued(@RequestParam List<Long> id){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Dish::getId,id);
        List<Dish> list = dishService.list(wrapper);
        for (Dish dish:list){
            dish.setStatus(0);
            dishService.updateById(dish);
        }
        return R.success("停售成功");
    }


    @PostMapping("/status/1")
    public R<String> startSelling(@RequestParam List<Long> id){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Dish::getId,id);
        List<Dish> list = dishService.list(wrapper);
        for (Dish dish:list){
            dish.setStatus(1);
            dishService.updateById(dish);
        }
        return R.success("起售成功");
    }


    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        dishService.removeWithFlavor(ids);
        return R.success("删除成功");
    }




    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        wrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(wrapper);
        return R.success(list);
    }
}
