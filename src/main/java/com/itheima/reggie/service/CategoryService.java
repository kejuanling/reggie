package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * 分类服务接口
 * 继承自MyBatis-Plus的IService，提供分类相关的业务操作
 */
public interface CategoryService extends IService<Category> {

    /**
     * 删除分类（包含关联校验）
     * @param id 分类ID
     */
    void deleteByCategoryId(Long id);
}
