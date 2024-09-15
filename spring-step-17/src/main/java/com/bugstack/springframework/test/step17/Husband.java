package com.bugstack.springframework.test.step17;

import java.util.Date;

public class Husband {
    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    private String wifiName;

    // 添加一个类型转换操作
    private Date marriageDate;
}
