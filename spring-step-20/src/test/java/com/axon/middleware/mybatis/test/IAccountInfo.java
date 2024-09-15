package com.axon.middleware.mybatis.test;

import java.util.List;

public interface IAccountInfo {
    AccountInfo queryUserInfoById(String name);

   List<AccountInfo> queryUserList();

}
