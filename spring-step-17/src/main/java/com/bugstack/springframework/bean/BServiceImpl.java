package com.bugstack.springframework.bean;

import com.bugstack.springframework.beans.factory.annotation.Autowired;
import com.bugstack.springframework.stereotype.Component;

@Component
public class BServiceImpl implements IBService {

    @Autowired
    private IAService iaService;

    @Override
    public void queryAService() {

        System.out.println("我是Aservice" + iaService);
    }
}
