package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void deleteByCategoryId(Long categoryId) {
        // 1. 查询是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,categoryId);
        int count1=dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("该分类下有菜品，不能删除");
        }

        // 2. 查询是否关联套餐
        LambdaQueryWrapper<Setmeal> setLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setLambdaQueryWrapper.eq(Setmeal::getCategoryId, categoryId);
        long count2 = setmealService.count(setLambdaQueryWrapper);

        if (count2 > 0) {
            throw new CustomException("该分类下有套餐，不能删除");
        }

        // 3. 都没关联，删除分类
        super.removeById(categoryId);
    }
}


