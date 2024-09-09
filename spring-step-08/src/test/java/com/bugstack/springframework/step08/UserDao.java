package com.bugstack.springframework.step08;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static Map<String, String> map = new HashMap<>();


    public void initMethod() {
        System.out.println("UserDao 中initMethod 方法");
        map.put("1", "收款单上岛咖啡");
        map.put("2", "我虞城县");
        map.put("3", "阿毛");
    }


    public void destroy() {
        System.out.println("执行 userDao-destroy 方法");
        map.clear();
    }


    public String queryUser(String userId) {
        return map.get(userId);
    }
}
