package com.lyc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyc.reggie.common.R;
import com.lyc.reggie.dto.SetmealDto;
import com.lyc.reggie.entity.Category;
import com.lyc.reggie.entity.Setmeal;
import com.lyc.reggie.service.CategoryService;
import com.lyc.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Date: 2022/5/24
 * Author: 3378
 * Description:
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithSetmealDish(setmealDto);
        return R.success("添加套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);

        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);

        setmealService.page(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list=records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item,setmealDto);

            Long id = item.getCategoryId();

            Category category = categoryService.getById(id);
            if(category!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }


    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }


    @PostMapping("/status/0")
    public R<String> discontinued(@RequestParam List<Long> ids){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        List<Setmeal> list = setmealService.list(wrapper);
        for (Setmeal s:list){
            s.setStatus(0);
            setmealService.updateById(s);
        }
        return R.success("修改成功");
    }

    @PostMapping("/status/1")
    public R<String> startSelling(@RequestParam List<Long> ids){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        List<Setmeal> list = setmealService.list(wrapper);
        for (Setmeal s:list){
            s.setStatus(1);
            setmealService.updateById(s);
        }
        return R.success("修改成功");
    }


}
