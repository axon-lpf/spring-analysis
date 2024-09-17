package com.axon.springframework.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static Map<String, String> map = new HashMap<>();

    static {
        map.put("1", "收款单上岛咖啡");
        map.put("2", "我虞城县");
        map.put("3", "阿毛");
    }

    public String queryUser(String userId) {
        return map.get(userId);
    }
}
