package com.bugstack.springframework.test.dao;

import com.bugstack.springframework.test.AccountInfo;

import java.util.List;

public interface IAccountInfo {
    AccountInfo queryUserInfoById(String name);

   List<AccountInfo> queryUserList();

}
