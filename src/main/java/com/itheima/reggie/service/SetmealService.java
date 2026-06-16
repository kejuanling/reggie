package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

/**
 * 套餐服务接口
 * 继承自MyBatis-Plus的IService，提供套餐相关的业务操作
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 更新套餐信息及关联的套餐菜品
     * @param setmealDto 套餐数据传输对象
     */
    void updateSetmealAndSetmealDish(SetmealDto setmealDto);

    /**
     * 保存套餐信息及关联的套餐菜品
     * @param setmealDto 套餐数据传输对象
     */
    void saveSetmealAndSetmealDish(SetmealDto setmealDto);

    /**
     * 删除套餐及关联的套餐菜品
     * @param ids 套餐ID列表
     */
    void removesetmealAndDish(List<Long> ids);

    /**
     * 根据套餐ID查询套餐信息及关联的套餐菜品
     * @param id 套餐ID
     * @return 套餐DTO对象
     */
    SetmealDto getSetmealAndSetMealDish(Long id);

    /**
     * 批量更新套餐状态
     * @param ids 套餐ID列表
     * @param statusNum 状态码（0-停售，1-启售）
     */
    void updateStatus(List<Long> ids, int statusNum);
}
