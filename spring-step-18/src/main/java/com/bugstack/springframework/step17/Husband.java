package com.bugstack.springframework.step17;

import java.time.LocalDate;

public class Husband {
    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    private String wifiName;

    public LocalDate getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    // 添加一个类型转换操作
    private LocalDate marriageDate;
}
