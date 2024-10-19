package com.axon.springframework.step14.bean;


import com.axon.springframework.stereotype.Component;

@Component("studentServiceImpl")

public class StudentServiceImpl implements IStudentService{
    @Override
    public void testQueryStudent() {
        System.out.println("我是student");
    }
}
