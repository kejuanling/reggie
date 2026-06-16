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

    @Override
    public ShoppingCart subShoppingCart(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(shoppingCart.getDishId()!=null, ShoppingCart::getDishId, shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart cartForMysql = super.getOne(queryWrapper);

        if(cartForMysql != null) {
            if(cartForMysql.getNumber() > 1){
                cartForMysql.setNumber(cartForMysql.getNumber() - 1);
                super.updateById(cartForMysql);
                return cartForMysql;
            } else {
                // 数量为1时，删除该记录
                super.removeById(cartForMysql.getId());
                cartForMysql.setNumber(0);
                return cartForMysql;
            }
        }
        return shoppingCart;
    }
}
