package com.axon.springframework.mybatis;

import com.axon.springframework.beans.factory.FactoryBean;
import com.axon.springframework.beans.factory.InitializingBean;
import com.axon.springframework.core.io.DefaultResourceLoader;
import com.axon.springframework.core.io.Resource;
import com.axon.springframework.mybatis.middleware.SqlSessionFactory;
import com.axon.springframework.mybatis.middleware.SqlSessionFactoryBuilder;

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
