package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/page")
    public R<Page<Category>> page(Integer page,Integer pageSize){
        Page<Category> pageInfo = new Page<Category>(page,pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("正在添加分类{}",category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类{}",category);
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete( @RequestParam  Long id){
        log.info("删除分类{}",id);
        categoryService.deleteByCategoryId(id);
        return R.success("删除成功");
    }
    //一级路径Category，二级路径List,参数是表单参数Integer
    @GetMapping("/list")
    public R<List<Category>> list(Integer type){
        //查询分类
        //1.创建查询对象
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //2.创建条件
        queryWrapper.eq(Category::getType,type);
        //3.执行查询
        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }



}
