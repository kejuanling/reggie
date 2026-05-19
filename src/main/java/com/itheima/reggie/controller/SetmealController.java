package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页管理
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(@RequestParam int page,
                                    @RequestParam int pageSize,
                                    @RequestParam(required = false) String name) {

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Setmeal> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        Wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, Wrapper);
        Page<SetmealDto> pageInfoDto=new Page<>(page, pageSize);
        BeanUtils.copyProperties(pageInfo,pageInfoDto,"records");
        // 转换 records
        List<SetmealDto> setmealDtoList = pageInfo.getRecords().stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Category category=categoryService.getById(setmeal.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());

        pageInfoDto.setRecords(setmealDtoList);
        return R.success(pageInfoDto);
    }

    /**
     * 增加套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveSetmealAndSetmealDish(setmealDto);
        return R.success("保存套餐成功");
    }

    /**
     * 修改套餐时的信息回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto setmealDto=setmealService.getSetmealAndSetMealDish(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.updateSetmealAndSetmealDish(setmealDto);
        return R.success("修改套餐成功");
    }

    /**
     * 修改套餐售卖状态
     * @param ids
     * @param statusNum
     * @return
     */
    @PostMapping("/status/{statusNum}")
    public R<String> status(@RequestParam List<Long> ids, @PathVariable int statusNum) {
        log.info("修改套餐状态：ids={}, status={}", ids, statusNum);
        setmealService.updateStatus(ids, statusNum);
        return R.success(statusNum == 0 ? "套餐停售成功" : "套餐启售成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("删除套餐：{}", ids);
        setmealService.removesetmealAndDish(ids);
        return R.success("删除套餐成功");
    }


}
