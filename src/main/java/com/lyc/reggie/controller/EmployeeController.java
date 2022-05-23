package com.lyc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyc.reggie.common.BaseContent;
import com.lyc.reggie.common.R;
import com.lyc.reggie.entity.Employee;
import com.lyc.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description:
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {

//        获取页面提交的密码,再进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

//        构造查询条件对象
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
//        根据传入的用户名,从数据库中查询到的对象
        Employee emp = employeeService.getOne(wrapper);

        if (emp == null) {
//            如果数据库中不存在该对象,返回失败结果
            return R.error("该账号不存在!");
        }

        if (!emp.getPassword().equals(password)) {
//            如果密码不一致,返回失败结果
            return R.error("密码错误!");
        }

        if (emp.getStatus() == 0) {
//            查看账号的状态码,决定是否成功登录
            return R.error("该账号已被停用!");
        }

//        登录条件都符合,将账号id存入session中
        request.getSession().setAttribute("employee", emp.getId());
//        将用户数据装入其中,返回成功消息对象
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));

        employeeService.save(employee);
        return R.success("创建成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);

        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        wrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee, HttpServletRequest request) {
//        employee.setUpdateTime(LocalDateTime.now());

//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
//        long id = Thread.currentThread().getId();
//        log.info("线程id{}",id);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");


    }

    @GetMapping("{id}")
    public R<Employee> getById(@PathVariable Long id) {

        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return R.error("未查询到该员工信息");
        }
        return R.success(employee);

    }


}
