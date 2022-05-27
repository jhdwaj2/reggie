package com.lyc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyc.reggie.common.BaseContent;
import com.lyc.reggie.common.R;
import com.lyc.reggie.dto.OrdersDto;
import com.lyc.reggie.entity.AddressBook;
import com.lyc.reggie.entity.OrderDetail;
import com.lyc.reggie.entity.Orders;
import com.lyc.reggie.entity.User;
import com.lyc.reggie.service.AddressBookService;
import com.lyc.reggie.service.OrderDetailService;
import com.lyc.reggie.service.OrdersService;
import com.lyc.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date: 2022/5/27
 * Author: 3378
 * Description:
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(Integer page,Integer pageSize){
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Orders::getUserId, BaseContent.getCurrentId());
        wrapper.orderByDesc(Orders::getOrderTime);

        ordersService.page(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto> dtoList=records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);

            Long addressBookId = item.getAddressBookId();
            AddressBook addressBook = addressBookService.getById(addressBookId);
            ordersDto.setPhone(addressBook.getPhone());
            ordersDto.setAddress(addressBook.getDetail());
            ordersDto.setConsignee(addressBook.getConsignee());

            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId,orderId);
            List<OrderDetail> list = orderDetailService.list(queryWrapper);

            ordersDto.setOrderDetails(list);

            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(dtoList);

        return R.success(ordersDtoPage);
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String number){
        Page<Orders> pageInfo = new Page<>(page,pageSize);

        Page<OrdersDto> ordersDtoPage = new Page<>();


        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(number!=null,Orders::getNumber,number);
        wrapper.orderByDesc(Orders::getOrderTime);

        ordersService.page(pageInfo,wrapper);

        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto> dtoList=records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            Long userId = item.getUserId();
            User user = userService.getById(userId);
            ordersDto.setUserName(user.getPhone());
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(dtoList);

        return R.success(ordersDtoPage);
    }

    @PutMapping
    public R<String> sendOrder(@RequestBody Orders orders){

        ordersService.updateById(orders);
        return R.success("派送成功");
    }


}
