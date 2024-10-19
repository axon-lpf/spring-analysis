package com.axon.springframework.test.step16.bean;

import com.axon.springframework.beans.factory.annotation.Autowired;
import com.axon.springframework.stereotype.Component;

@Component
public class BServiceImpl implements IBService {

    @Autowired
    private IAService iaService;

    @Override
    public void queryAService() {

        System.out.println("我是Aservice" + iaService);
    }
}
