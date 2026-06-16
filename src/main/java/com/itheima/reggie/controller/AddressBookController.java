package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success("保存成功");

    }

    @PutMapping("/default")
    public R<String> updateDefault(@RequestBody AddressBook addressBook) {
        addressBookService.updateDefault(addressBook);
        return R.success("设置默认地址成功");
    }

    @GetMapping
    public R<AddressBook> findById(@RequestParam Long id) {
        AddressBook addressBook=addressBookService.getById(id);
        return R.success(addressBook);
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook=addressBookService.getOne(queryWrapper);

        if(addressBook==null){
            // 如果没有默认地址，获取用户最新的一条地址
            LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
            wrapper.orderByDesc(AddressBook::getUpdateTime);
            wrapper.last("limit 1");
            addressBook = addressBookService.getOne(wrapper);

            if(addressBook == null){
                return R.error("请先添加收货地址");
            }
            // 自动设为默认
            addressBook.setIsDefault(1);
            addressBookService.updateById(addressBook);
        }
        return R.success(addressBook);
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        LambdaQueryWrapper<AddressBook> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDeleted,0);
        List<AddressBook> list=addressBookService.list(queryWrapper);
        if(list.isEmpty()){
            throw new CustomException("暂无地址");
        }
        return R.success(list);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam Long id) {
        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBook.setIsDeleted(1);
        addressBookService.updateById(addressBook);
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }
}
