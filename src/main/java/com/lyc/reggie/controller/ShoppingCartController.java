package com.lyc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lyc.reggie.common.BaseContent;
import com.lyc.reggie.common.R;
import com.lyc.reggie.entity.ShoppingCart;
import com.lyc.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Date: 2022/5/25
 * Author: 3378
 * Description:
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {


    @Autowired
    private ShoppingCartService shoppingCartService;


    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
//        获取用户id
        Long userId = BaseContent.getCurrentId();
        log.info("-----------{}----------------",userId);
        shoppingCart.setUserId(userId);

//        尝试获取购物车中的菜品id
        Long dishId = shoppingCart.getDishId();

//        匹配当前用户的购物车
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);

//            判断用户添加的是菜品还是套餐
        if(dishId==null){
//            添加的是套餐
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }else{
//            添加的是菜品
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }

//        获取用户包含所点的菜品/套餐的购物车信息
        ShoppingCart cartServiceOne = shoppingCartService.getOne(wrapper);

//        第一次添加该菜品/套餐
        if (cartServiceOne==null){
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cartServiceOne=shoppingCart;
        }else{
//            购物车已有该菜品/套餐
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(cartServiceOne);
        }

        return R.success(cartServiceOne);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        Long currentId = BaseContent.getCurrentId();
        wrapper.eq(ShoppingCart::getUserId,currentId);
        wrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        Long currentId = BaseContent.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,currentId);

        shoppingCartService.remove(wrapper);
        return R.success("清空成功");
    }

    @PostMapping("/sub")
    public R<String> delete(@RequestBody ShoppingCart shoppingCart){
        Long currentId = BaseContent.getCurrentId();

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,currentId);

        Long dishId = shoppingCart.getDishId();

        if(dishId==null){
//            套餐
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }else{
//            菜品
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(wrapper);

        if(cartServiceOne==null){
            return R.error("请稍后重试");
        }

        Integer number = cartServiceOne.getNumber();


        if (number==1){
            shoppingCartService.removeById(cartServiceOne.getId());
            return R.success("删除成功");
        }

        if (number>1){
            cartServiceOne.setNumber(number-1);
            shoppingCartService.updateById(cartServiceOne);
            return R.success("删除成功");
        }

        return R.error("请稍后重试");

    }

}
