package com.axon.springframework.step15;


import com.axon.springframework.stereotype.Component;


public class StudentServiceImpl implements IStudentService {
    @Override
    public void testQueryStudent() {
        System.out.println("我是student");
    }
}
