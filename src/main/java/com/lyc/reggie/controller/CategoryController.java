package com.lyc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyc.reggie.common.R;
import com.lyc.reggie.entity.Category;
import com.lyc.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Date: 2022/5/18
 * Author: 3378
 * Description:
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {


    @Autowired
    private CategoryService categoryService;



    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        log.info("page={},pageSize={}", page, pageSize);
        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }


    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类,id为{}",ids);

        categoryService.remove(ids);

        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息:{}",category);
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(category.getType()!=null,Category::getType,category.getType());

        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(wrapper);

        return R.success(list);
    }




}
