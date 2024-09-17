package com.axon.springframework.jdbc.supoort;

import cn.hutool.core.lang.Assert;
import com.axon.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;

public abstract class JdbcAccess implements InitializingBean {


    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected DataSource obtainDataSource() {
        DataSource dataSource = getDataSource();
        Assert.state(dataSource != null, "No DataSource set");
        return dataSource;
    }

    @Override
    public void afterPropertiesSet() {
        if (getDataSource() == null) {
            throw new RuntimeException("数据库配置未初始化");
        }

    }
}
