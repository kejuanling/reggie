package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.mapper.ShoppingCartMapper;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public ShoppingCart savaShoppingCart(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper1 = new LambdaQueryWrapper<ShoppingCart>();
        queryWrapper1.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper1.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper1.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart cartForMysql=super.getOne(queryWrapper1);
        if(cartForMysql!=null){
            cartForMysql.setNumber(cartForMysql.getNumber()+1);
            super.updateById(cartForMysql);
            return cartForMysql;
        }else {
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setNumber(1);
            super.save(shoppingCart);
            return shoppingCart;
        }
    }
}
