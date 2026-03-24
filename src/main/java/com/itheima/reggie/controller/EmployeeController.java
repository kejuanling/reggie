package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}