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
    public R<String> updateDefalt(@RequestBody AddressBook addressBook) {
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
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook=addressBookService.getOne(queryWrapper);
        if(addressBook==null){
            return R.error("请先设置默认地址");
        }
        return R.success(addressBook);
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        LambdaQueryWrapper<AddressBook> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDeleted,0);
        List<AddressBook> list=addressBookService.list(queryWrapper);
        if(list.size()<=0){
            throw new CustomException("暂无地址");
        }
        return R.success(list);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam Long ids) {
        LambdaQueryWrapper<AddressBook> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getIsDeleted,1);
        queryWrapper.eq(AddressBook::getId,ids);
        addressBookService.update(queryWrapper);
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }
}
