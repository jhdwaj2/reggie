package com.lyc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.reggie.dto.DishDto;
import com.lyc.reggie.entity.AddressBook;
import com.lyc.reggie.entity.Dish;
import com.lyc.reggie.entity.DishFlavor;
import com.lyc.reggie.mapper.AddressBookMapper;
import com.lyc.reggie.mapper.DishMapper;
import com.lyc.reggie.service.AddressBookService;
import com.lyc.reggie.service.DishFlavorService;
import com.lyc.reggie.service.DishService;
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
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
