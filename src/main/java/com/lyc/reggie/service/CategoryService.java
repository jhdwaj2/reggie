package com.lyc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyc.reggie.entity.Category;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
public interface CategoryService extends IService<Category> {


    public void remove(Long id);


}
