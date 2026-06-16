package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrderService;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户提交订单
     * @param orders 订单对象
     * @return 响应结果
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        if (orders == null) {
            return R.error("订单对象不能为空");
        }
        orderService.submit(orders);
        return R.success("订单生成成功");
    }

    /**
     * 订单分页查询（管理端）
     * @param page 页码
     * @param pageSize 每页大小
     * @param number 订单号（可选）
     * @return 分页订单列表
     */
    @GetMapping("/page")
    public R<Page<Orders>> page(@RequestParam int page,
                                @RequestParam int pageSize,
                                @RequestParam(required = false) String number) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(number), Orders::getNumber, number);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);
        if (pageInfo.getRecords() == null || pageInfo.getRecords().isEmpty()) {
            return R.success(null);
        }
        return R.success(pageInfo);
    }

    /**
     * 更新订单状态（取消、派送、完成）
     * @param orders 订单对象（包含id和status）
     * @return 响应结果
     */
    @PutMapping
    public R<String> update(@RequestBody Orders orders) {
        if (orders == null || orders.getId() == null || orders.getStatus() == null) {
            return R.error("订单对象或状态不能为空");
        }
        log.info("更新订单状态：{}", orders);
        orderService.updateById(orders);
        return R.success("订单状态更新成功");
    }

    /**
     * 用户订单分页查询（移动端）
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分页订单列表（包含订单明细）
     */
    @GetMapping("/userPage")
    public R<Page<Orders>> userPage(@RequestParam int page, @RequestParam int pageSize) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return R.error("用户未登录");
        }
        
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);
        
        // 为每个订单填充订单明细
        List<Orders> ordersList = pageInfo.getRecords();
        if (ordersList != null && !ordersList.isEmpty()) {
            ordersList.forEach(order -> {
                LambdaQueryWrapper<OrderDetail> detailWrapper = new LambdaQueryWrapper<>();
                detailWrapper.eq(OrderDetail::getOrderId, order.getId());
                List<OrderDetail> details = orderDetailService.list(detailWrapper);
                order.setOrderDetails(details);
            });
        }
        
        return R.success(pageInfo);
    }

    /**
     * 再来一单
     * @param data 包含订单ID的对象
     * @return 响应结果
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders data) {
        if (data == null || data.getId() == null) {
            return R.error("订单ID不能为空");
        }
        orderService.againOrder(data.getId());
        return R.success("添加购物车成功");
    }
}