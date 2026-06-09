package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.controller.CategoryController;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private CategoryController categoryController;

    @Override
    @Transactional
    public void submit(Orders orders) {
    LambdaQueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new LambdaQueryWrapper<ShoppingCart>();
    shoppingCartQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
    List<ShoppingCart> list=shoppingCartService.list(shoppingCartQueryWrapper);
    if(list==null||list.size()<=0){
        throw new CustomException("购物车中无数据，无法生成订单");
    }

    orders.setNumber(IdWorker.getIdStr());
    orders.setStatus(1);
    orders.setUserId(BaseContext.getCurrentId());
    BigDecimal amount = new BigDecimal("0");
    for (ShoppingCart shoppingCart : list) {
        BigDecimal num=new BigDecimal(shoppingCart.getNumber());
        BigDecimal cartAmount=shoppingCart.getAmount();
        BigDecimal multiply=num.multiply(cartAmount);
        amount=amount.add(multiply);
    }
    orders.setAmount(amount);
    AddressBook addressBook=addressBookService.getById(orders.getAddressBookId());
    if(addressBook!=null){
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
    }
    User user=userService.getById(BaseContext.getCurrentId());
    if(user!=null){
        orders.setUserName(user.getName());
    }
    orders.setOrderTime(LocalDateTime.now());
    orders.setCheckoutTime(LocalDateTime.now());
    super.save(orders);

    List<OrderDetail> detailList=list.stream().map(shoppingCart -> {
        OrderDetail orderDetail=new OrderDetail();
        BeanUtils.copyProperties(shoppingCart,orderDetail,"id");
        orderDetail.setOrderId(orders.getId());
        return orderDetail;
    }).collect(Collectors.toList());
    orderDetailService.saveBatch(detailList);

    LambdaQueryWrapper<ShoppingCart> updateWrapper=new LambdaQueryWrapper<>();
    updateWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
    shoppingCartService.remove(updateWrapper);

    }
}