package com.bugstack.springframework.bean;

/**
 * 老公类
 */
public class Husband {

    private Wife wife;

    public String queryWife() {
        return "Husband.wife";
    }

    public void setWife(Wife wife) {
     this.wife = wife;
    }
}
