package com.lyc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.reggie.entity.User;
import com.lyc.reggie.mapper.UserMapper;
import com.lyc.reggie.service.UserService;
import org.springframework.stereotype.Service;


/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
