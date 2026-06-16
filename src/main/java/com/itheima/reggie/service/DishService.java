package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

/**
 * 菜品服务接口
 * 继承自MyBatis-Plus的IService，提供菜品相关的业务操作
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品（包含口味信息）
     * @param dishDto 菜品数据传输对象，包含菜品基础信息和口味列表
     */
    void saveDishAndFlavors(DishDto dishDto);

    /**
     * 根据菜品ID查询菜品信息和对应的口味信息
     * @param id 菜品ID
     * @return 菜品DTO对象，包含口味列表
     */
    DishDto getDishDtoByDishId(Long id);

    /**
     * 更新菜品信息，同时更新对应的口味信息
     * @param dishDto 菜品数据传输对象
     */
    void updateDishAndDishFlavors(DishDto dishDto);

    /**
     * 删除菜品（包含关联的口味信息）
     * @param ids 菜品ID列表
     */
    void removeDish(List<Long> ids);

    /**
     * 根据菜品条件查询菜品DTO列表
     * @param dish 菜品查询条件
     * @return 菜品DTO列表
     */
    List<DishDto> getDishDtosByDishId(Dish dish);
}
