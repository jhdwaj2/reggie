package com.lyc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.reggie.entity.OrderDetail;
import com.lyc.reggie.entity.Orders;
import com.lyc.reggie.mapper.OrderDetailMapper;
import com.lyc.reggie.mapper.OrdersMapper;
import com.lyc.reggie.service.OrderDetailService;
import com.lyc.reggie.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
