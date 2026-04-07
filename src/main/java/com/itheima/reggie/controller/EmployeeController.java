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
import java.time.LocalDateTime;

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

    /**
     * 新增员工：一级路径是employee，没有二级路径
     * @param employee
     * @return
     */
    @PostMapping
    public R save(@RequestBody Employee employee,HttpSession session){
        log.info("新增员工,员工信息:{}",employee.toString());
        //1.设置员工状态
        employee.setStatus(1);
        //2.设置创建和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //3.获取员工id
        Long userId = (Long) session.getAttribute("employee");
        employee.setCreateUser(userId);
        employee.setUpdateUser(userId);
        //4.设置密码
        String password=DigestUtils.md5DigestAsHex("000000".getBytes());
        employee.setPassword(password);
        //5.保存到数据库
        employeeService.save(employee);
        //6.返回成功信息
        return R.success(employee+"新增员工成功");
    }
    /**
     * 员工信息回显：一级路径是employee,二级没有
     * 请求方式：get；请求参数：路径参数，员工id
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
       return R.error("没有查询到员工信息");
    }

    /**
     * 修改员工信息
     * 请求方式：put；请求参数：json
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpSession session){
        //1.修改上传时间和修改人员
        employee.setUpdateTime(LocalDateTime.now());
        //2.获取员工id
        Long userid =(Long) session.getAttribute("employee");
        employee.setUpdateUser(userid);
        //3.更新员工信息
        employeeService.updateById(employee);

        return R.success("修改成功");
    }

}