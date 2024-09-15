package com.axon.middleware.mybatis.test;

import com.alibaba.fastjson.JSON;
import com.axon.middleware.mybatis.Resources;
import com.axon.middleware.mybatis.SqlSession;
import com.axon.middleware.mybatis.SqlSessionFactory;
import com.axon.middleware.mybatis.SqlSessionFactoryBuilder;
import org.testng.annotations.Test;

import java.io.Reader;
import java.util.List;

public class ApiTest {

    /**
     * mybatis中sql执行核心流程
     * 1. 读取配置，解析xml文件
     * 2.获取其中的sql, 请求参数，入参类型，返回类类型， namespace命名空间
     * 3.构建sql,将请求参数拼接到sql上。
     * 4.请求数据库，查询结果。
     * 5.解析结果值，并组装返回结果值，返回给对应的接口。
     */
    @Test
    public void test_queryUserInfoById() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

            SqlSession session = sqlMapper.openSession();
            try {
                AccountInfo user = session.selectOne("com.axon.middleware.mybatis.test.IAccountInfo.queryUserInfoById", "老狗");
                System.out.println(JSON.toJSONString(user));
            } finally {
                session.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test_queryUserList() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

            SqlSession session = sqlMapper.openSession();
            try {
                List<AccountInfo> userList = session.selectList("com.axon.middleware.mybatis.test.IAccountInfo.queryUserList", "黑狗1");
                System.out.println(JSON.toJSONString(userList));
            } finally {
                session.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
