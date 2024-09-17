package com.axon.springframework.test;

import java.math.BigDecimal;

public class AccountInfo {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;


    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    private BigDecimal balance;
}
