package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import org.springframework.web.bind.annotation.DeleteMapping;

public interface CategoryService extends IService<Category> {
    void deleteByCategoryId(Long id);
}
