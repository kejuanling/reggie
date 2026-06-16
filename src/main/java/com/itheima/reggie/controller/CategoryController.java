package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
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

    /**
     * 菜品分类查询
     * @param category
     * @return
     */
    //一级路径Category，二级路径List,参数是表单参数Integer
    /**
     * 根据类型查询分类列表
     * @param category 分类对象（包含type字段）
     * @return 分类列表
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 如果type不为null，则按类型过滤；否则返回所有分类
        if (category != null && category.getType() != null) {
            queryWrapper.eq(Category::getType, category.getType());
        }
        queryWrapper.orderByAsc(Category::getSort);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}
