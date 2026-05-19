package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void updateSetmealAndSetmealDish(SetmealDto setmealDto) ;

    void saveSetmealAndSetmealDish(SetmealDto setmealDto);

    void removesetmealAndDish(List<Long> ids);

    SetmealDto getSetmealAndSetMealDish(Long id);

    void updateStatus(List<Long> ids, int statusNum);
}
