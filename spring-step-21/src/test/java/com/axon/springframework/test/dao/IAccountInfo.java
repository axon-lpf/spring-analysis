package com.axon.springframework.test.dao;

import com.axon.springframework.test.AccountInfo;

import java.util.List;

public interface IAccountInfo {
    AccountInfo queryUserInfoById(String name);

   List<AccountInfo> queryUserList();

}
