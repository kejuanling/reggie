package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单明细控制器
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 根据订单ID查询订单明细列表
     * @param id 订单ID
     * @return 订单明细列表
     */
    @GetMapping("/{id}")
    public R<List<OrderDetail>> getByOrderId(@PathVariable Long id) {
        log.info("查询订单明细，订单ID：{}", id);
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> list = orderDetailService.list(queryWrapper);
        if (list == null || list.isEmpty()) {
            return R.success(null);
        }
        return R.success(list);
    }
}