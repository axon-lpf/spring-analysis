package com.bugstack.springframework.mybatis;

import com.bugstack.springframework.beans.factory.FactoryBean;
import com.bugstack.springframework.beans.factory.InitializingBean;
import com.bugstack.springframework.core.io.DefaultResourceLoader;
import com.bugstack.springframework.core.io.Resource;
import com.bugstack.springframework.mybatis.middleware.SqlSessionFactory;
import com.bugstack.springframework.mybatis.middleware.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean {



    private String resource;

    private SqlSessionFactory sqlSessionFactory;


    @Override
    public SqlSessionFactory getObject() throws Exception {
        return sqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return SqlSessionFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {

        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        Resource resource = defaultResourceLoader.getResource(this.resource);
        try (InputStream inputStream = resource.getInputStream()) {
            this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
