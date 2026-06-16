package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.mapper.AddressBookMapper;
import com.itheima.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;


@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Override
    public void updateDefault(AddressBook addressBook) {
        // 1. 先清除该用户所有地址的默认状态
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault, 0);
        super.update(updateWrapper);

        // 2. 再设置指定地址为默认
        LambdaUpdateWrapper<AddressBook> updateWrapper2 = new LambdaUpdateWrapper<>();
        updateWrapper2.eq(AddressBook::getId, addressBook.getId());
        updateWrapper2.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        updateWrapper2.set(AddressBook::getIsDefault, 1);
        super.update(updateWrapper2);
    }
}
