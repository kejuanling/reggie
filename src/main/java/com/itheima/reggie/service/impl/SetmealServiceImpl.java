package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private  SetmealDishService setmealDishService;

    /**
     * 套餐信息回显
     * @param id
     * @return
     */
    @Override
    public SetmealDto getSetmealAndSetMealDish(Long id) {
        Setmeal setmeal = super.getById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList=setmealDishService.list(queryWrapper);
        SetmealDto setmealDto=new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    /**
     * 修改/更新套餐
     * @param setmealDto
     */
    @Transactional
    @Override
    public void updateSetmealAndSetmealDish(SetmealDto setmealDto) {
        super.updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
//        建立口味与菜品的联系
        List<SetmealDish> setmealDishList=setmealDto.getSetmealDishes().stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());//设置套餐id
            setmealDish.setId(null);//清空id，让数据库自动生成
            return setmealDish;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }

    /**
     * 添加套餐
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveSetmealAndSetmealDish(SetmealDto setmealDto) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getName,setmealDto.getName());
        Setmeal setmeal = super.getOne(queryWrapper);
        if(setmeal!=null){
            throw new CustomException("添加失败，套餐已存在");
        }
        super.save(setmealDto);
        List<SetmealDish> setmealDishList=setmealDto.getSetmealDishes().stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }

    /**
     * 修改套餐转态
     * @param ids
     * @param status
     */
    @Override
    public void updateStatus(List<Long> ids, int status) {
        // 创建更新条件构造器
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        // 设置更新条件：WHERE id IN (ids集合)
        updateWrapper.in(Setmeal::getId, ids);
        // 设置要更新的字段：SET status = status
        updateWrapper.set(Setmeal::getStatus, status);
        // 执行更新操作
        this.update(updateWrapper);
        // 记录日志
        log.info("批量修改套餐状态：ids={}, status={}", ids, status);
    }

    /**
     * 删除套餐
     * @param ids
     */
    @Transactional
    @Override
    public void removesetmealAndDish(List<Long> ids) {
        // 1. 检查是否有启售状态的套餐
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1); // 启售状态
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("待删除的套餐中，存在启售状态的套餐，不能删除");
        }
        // 2. 删除套餐基本信息
        this.removeByIds(ids);
        // 3. 删除套餐关联的菜品关系
        LambdaQueryWrapper<SetmealDish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(dishQueryWrapper);
    }

}
