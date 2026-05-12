package com.itheima.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> page(int page,int pageSize,String name){
        //构造分页构造器对象
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<DishDto>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(dishPage,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        List<Dish> records = dishPage.getRecords();
        List<DishDto> dishDtoList=records.stream().map((dish)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id回显菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getDishDtoByDishId(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateDishAndDishFlavors(dishDto);
        return R.success("修改菜品成功");
    }

//    /**
//     *菜品修改状态
//     * @param status
//     * @param ids
//     * @return
//     */
//    @PostMapping("status/{status}")
//    public R<String> updateSatus(@PathVariable Integer status,@RequestParam  List<Long> ids){
//        dishService.updateDishStatus(status,ids);
//        return R.success("菜品修改成功");
//    }

    /**
     * 菜品修改状态（批量停售/启售）
     * @param ids 菜品ID集合（通过请求参数传递，格式：?ids=1,2,3）
     * @param statusNum 状态码（通过路径传递，0=停售，1=启售）
     * @return 统一响应结果
     */
    @PostMapping("/status/{statusNum}")
    public R<String> status(@RequestParam List<Long> ids, @PathVariable int statusNum){
        // 1. 创建更新条件构造器（用于构建UPDATE语句）
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        // 2. 设置更新条件：WHERE id IN (ids集合)
        updateWrapper.in(Dish::getId, ids);
        // 3. 设置要更新的字段：SET status = statusNum
        updateWrapper.set(Dish::getStatus, statusNum);
        // 4. 执行更新操作
        dishService.update(updateWrapper);
        // 5. 返回成功提示（根据状态码返回不同文案）
        return R.success(statusNum == 0 ? "菜品停售成功" : "启售成功");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("删除菜品：{}", ids);
        dishService.removeDish(ids);
        return R.success("删除菜品成功");
    }
}
