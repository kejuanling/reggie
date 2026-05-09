package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    public void saveDishAndFlavors(DishDto dishDto);
    //根据id查询菜品信息和对应的口味信息
    public DishDto getDishDtoByDishId(Long id);
    //更新菜品信息，同时更新对应的口味信息
    public void updateDishAndDishFlavors(DishDto dishDto);
}
