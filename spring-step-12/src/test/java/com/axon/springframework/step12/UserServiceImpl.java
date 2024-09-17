package com.axon.springframework.step12;

public class UserServiceImpl implements IUserService{
    @Override
    public String queryUserInfo() {
        return "我是一只小傻瓜";
    }

    @Override
    public String register(String userName) {
        return userName + "是注册用户";
    }
}
