package com.lyc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lyc.reggie.Utils.ValidateCodeUtils;
import com.lyc.reggie.common.BaseContent;
import com.lyc.reggie.common.R;
import com.lyc.reggie.entity.User;
import com.lyc.reggie.service.SendMailService;
import com.lyc.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Date: 2022/5/25
 * Author: 3378
 * Description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private UserService userService;


    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String emailAddress = user.getPhone();

        if (StringUtils.isNotEmpty(emailAddress)) {
//            生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("验证码:{}",code);
//            发送邮箱验证码
            sendMailService.sendMail(emailAddress, code);
//            将生成的验证码存入Session
            session.setAttribute(emailAddress, code);

        } else {
            return R.error("邮箱地址为空!");
        }
        log.info("--------------{}----------------", BaseContent.getCurrentId());
        return R.success("验证码发送成功!");
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody Map<String, String> map, HttpSession session) {
//        if (1 != 0) {
//            session.setAttribute("user", 1529371936025513986L);
//            return R.success("登陆成功");
//        }
        log.info(map.toString());
//        获取手机号,用户输入验证码
        String phone = map.get("phone");
        String UserCode = map.get("code");

        String code = (String) session.getAttribute(phone);
        if (code.equals(UserCode)) {

            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            log.info("--------------{}----------------", BaseContent.getCurrentId());
            return R.success("登录成功");
        } else {
            return R.error("验证码错误!");
        }
    }

    @PostMapping("/logout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("user");
        return R.success("推出成功");
    }

}
