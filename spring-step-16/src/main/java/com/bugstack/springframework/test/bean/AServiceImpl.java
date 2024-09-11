package com.bugstack.springframework.test.bean;

import com.bugstack.springframework.beans.factory.annotation.Autowired;
import com.bugstack.springframework.stereotype.Component;

@Component
public class AServiceImpl implements IAService {

    @Autowired
    private IBService bSService;

    @Override
    public void queryBService() {
        System.out.println("查询到BService" + bSService);
    }
}
