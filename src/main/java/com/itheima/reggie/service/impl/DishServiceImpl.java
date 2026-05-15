package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>  implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品实现类
     * @param dishDto
     */
    @Transactional //涉及多张表，有事务，添加注解
    @Override
    public void saveDishAndFlavors(DishDto dishDto) {
//  查询菜品是否存在
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getName, dishDto.getName());
        Dish dish=super.getOne(queryWrapper);
        if(dish!=null){
            throw new CustomException("菜品已存在");
        }
        //1.保存菜品表
        super.save(dishDto);
        //2.保存口味表
        List<DishFlavor> flavorList = dishDto.getFlavors().stream().map(dishFlavor->{
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavorList);
    }

    @Override
    public DishDto getDishDtoByDishId(Long id) {
//        1、查询dish
        Dish dish=super.getById(id);
//        2.查询flavors：根据dish的id查询对应口味
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper=new LambdaQueryWrapper<>();
//        添加查询条件
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
//        查询列表
        List<DishFlavor> dishFlavors=dishFlavorService.list(dishFlavorLambdaQueryWrapper);
//        3.组装到dishDto
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
//        手动设置口味集合
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    @Transactional
    @Override
    public void updateDishAndDishFlavors(DishDto dishDto) {
//        1.修改dish表
        super.updateById(dishDto);
//        2.口味表
//        删除当前菜品的所有口味
        LambdaQueryWrapper<DishFlavor> updateWrapper=new LambdaQueryWrapper<>();
        updateWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(updateWrapper);
//        建立口味与菜品的联系
        List<DishFlavor> flavorList=dishDto.getFlavors().stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavorList);
    }

//    @Override
//    public void updateDishStatus(Integer status, List<Long> ids) {
//        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
//        // 设置要更新的状态
//        updateWrapper.set(Dish::getStatus, status);
//        // 设置要更新的菜品id
//        updateWrapper.in(Dish::getId, ids);
//        // 执行更新
//        this.update(updateWrapper);
//    }

    @Transactional
    @Override
    public void removeDish(List<Long> ids) {
//      1. 判断菜品状态，如果有一个是1（启用），不能删除
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        queryWrapper.eq(Dish::getStatus, 1); // 启售状态
        long count = this.count(queryWrapper);
        if (count > 0) {
            // 待删除的集合中，有起售状态的菜品
            throw new CustomException("待删除的菜品中，存在启售状态的菜品，不能删除");
        }
//         2. 删除菜品表的数据
        this.removeByIds(ids);
//         3. 删除口味表的数据
        LambdaQueryWrapper<DishFlavor> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(updateWrapper);
    }
}
