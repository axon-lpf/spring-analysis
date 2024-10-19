package com.axon.springframework.step14.bean;


import com.axon.springframework.beans.factory.annotation.Autowired;
import com.axon.springframework.beans.factory.annotation.Value;
import com.axon.springframework.stereotype.Component;

import java.util.Random;

@Component("userService")
public class UserService implements IUserService {


    @Autowired
    private IStudentService studentServiceImpl;

    @Value("${token}")
    private String token;

    @Override
    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        studentServiceImpl.testQueryStudent();
        return "我是一只大傻狗" + token;
    }

    @Override
    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + " success！";
    }

    @Override
    public String toString() {
        return "UserService#token = { " + token + " }";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}