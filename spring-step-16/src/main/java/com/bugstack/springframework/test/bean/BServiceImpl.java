package com.bugstack.springframework.test.bean;

import com.bugstack.springframework.beans.factory.annotation.Autowired;
import com.bugstack.springframework.stereotype.Component;
import com.bugstack.springframework.stereotype.Service;

@Component
public class BServiceImpl implements IBService {

    @Autowired
    private IAService iaService;

    @Override
    public void queryAService() {

        System.out.println("我是Aservice" + iaService);
    }
}
