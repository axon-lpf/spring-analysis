package com.axon.springframework.test.bean;

import com.axon.springframework.beans.factory.annotation.Autowired;
import com.axon.springframework.stereotype.Component;

@Component
public class AServiceImpl implements IAService {

    @Autowired
    private IBService bSService;

    @Override
    public void queryBService() {
        System.out.println("查询到BService" + bSService);
    }
}
