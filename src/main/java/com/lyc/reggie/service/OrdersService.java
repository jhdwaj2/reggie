package com.lyc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyc.reggie.dto.DishDto;
import com.lyc.reggie.entity.Dish;
import com.lyc.reggie.entity.Orders;

import java.util.List;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);
}
