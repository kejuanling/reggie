package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void submit(Orders orders) {
//    LambdaQueryWrapper<Orders> shoppingCartQueryWrapper = new LambdaQueryWrapper<Orders>();
//    shoppingCartQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
//    List<ShoppingCart> list=shoppingCartService.list(shoppingCartQueryWrapper);
//    if(list==null||list.size()<=0){
//        throw new CustomException("购物车中无数据，无法生成订单");
//    }

    }
}