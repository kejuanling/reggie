package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpSession session){
        log.info("员工登录");
        //1.密码加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.各种校验
        if(emp == null){
            return R.error("用户不存在");
        }
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        if (emp.getStatus()==0){
            return R.error("用户被禁用");
        }

        //4.登录成功，把员工信息存入session
        session.setAttribute("employee",emp.getId());

        return R.success(emp);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpSession session) {
        //使用session的invalidate()卷除session所有的key
        session.invalidate();
        return R.success("退出成功");
    }

    //员工分页查询
    //一级路径：emplouyee 二级：page 方法：get
    //参数是表单参数：page,pageSize,name
    //分页有两步：注册mybatisplus的分页器；进行分页查询
    @GetMapping("/page")
    public R page(Integer page,Integer pageSize,String name){
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行带条件的分页查询：会执行两条查询——一个查询页，一个查询总数
        //会保存到pageinfo
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
}