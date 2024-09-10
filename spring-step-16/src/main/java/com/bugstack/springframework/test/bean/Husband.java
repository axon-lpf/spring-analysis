package com.bugstack.springframework.test.bean;

/**
 * 老公类
 */
public class Husband {

    private Wife wife;

    public String queryWife() {
        return "Husband.wife";
    }

}
