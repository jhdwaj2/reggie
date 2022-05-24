package com.lyc.reggie.dto;

import com.lyc.reggie.entity.Setmeal;
import com.lyc.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
