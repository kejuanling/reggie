package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增菜品
    public void saveDishAndFlavors(DishDto dishDto);
    //根据id查询菜品信息和对应的口味信息
    public DishDto getDishDtoByDishId(Long id);
    //更新菜品信息，同时更新对应的口味信息
    public void updateDishAndDishFlavors(DishDto dishDto);
    //菜品售卖状态
//    void updateDishStatus(Integer status, List<Long> ids);
//    删除菜品
    void removeDish(List<Long> ids);
    List<DishDto>  getDishDtosByDishId(Dish dish);
}
