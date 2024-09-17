package com.axon.springframework.jdbc.datasource;

import cn.hutool.core.lang.Assert;
import com.axon.springframework.beans.factory.InitializingBean;
import com.axon.springframework.tx.transaction.CannotCreateTransactionException;
import com.axon.springframework.tx.transaction.TransactionDefinition;
import com.axon.springframework.tx.transaction.TransactionException;
import com.axon.springframework.tx.transaction.support.AbstractPlatformTransactionManager;
import com.axon.springframework.tx.transaction.support.DefaultTransactionStatus;
import com.axon.springframework.tx.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceTransactionManager extends AbstractPlatformTransactionManager implements InitializingBean {


    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private DataSource dataSource;

    public DataSourceTransactionManager() {
    }

    public DataSourceTransactionManager(DataSource dataSource) {
        setDataSource(dataSource);
        afterPropertiesSet();
    }

    protected DataSource obtainDataSource() {
        DataSource dataSource = getDataSource();
        Assert.notNull(dataSource, "No DataSource set");
        return dataSource;
    }

    /**
     * 核心逻辑-获取一个事物
     *
     * @return
     * @throws TransactionException
     */
    @Override
    protected Object doGetTransaction() throws TransactionException {
        DataSourceTransactionObject txObject = new DataSourceTransactionObject();
        ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(obtainDataSource());
        txObject.setConnectionHolder(conHolder, false);
        return txObject;
    }

    /**
     * 核心逻辑-提交事物
     *
     * @param status
     * @throws TransactionException
     */
    @Override
    protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) status.getTransaction();
        Connection con = txObject.getConnectionHolder().getConnection();
        try {
            con.commit();
        } catch (SQLException e) {
            throw new TransactionException("Could not commit JDBC transaction", e);
        }
    }

    /**
     * 核心逻辑-回滚事务
     *
     * @param status
     * @throws TransactionException
     */
    @Override
    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) status.getTransaction();
        Connection con = txObject.getConnectionHolder().getConnection();
        try {
            con.rollback();
        } catch (SQLException e) {
            throw new TransactionException("Could not roll back JDBC transaction", e);
        }
    }

    /**
     * 核心逻辑- 事务的链接
     * @param transaction
     * @param definition
     * @throws TransactionException
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
        Connection con = null;
        try {
            Connection newCon = obtainDataSource().getConnection();
            txObject.setConnectionHolder(new ConnectionHolder(newCon), true);

            con = txObject.getConnectionHolder().getConnection();
            if (con.getAutoCommit()) {
                con.setAutoCommit(false);
            }
            prepareTransactionalConnection(con, definition);

            // 这里去设置 当前线程的数据库连接
            TransactionSynchronizationManager.bindResource(obtainDataSource(), txObject.getConnectionHolder());

        } catch (SQLException e) {
            try {
                assert con != null;
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            txObject.setConnectionHolder(null, false);
            throw new CannotCreateTransactionException("Could not open JDBC Connection for transaction", e);
        }

    }

    @Override
    public void afterPropertiesSet() {
        if (null == getDataSource()) {
            throw new IllegalArgumentException("Property 'datasource' is required");
        }
    }

    protected void prepareTransactionalConnection(Connection con, TransactionDefinition definition) throws SQLException {
        if (definition.isReadOnly()) {
            try (Statement stmt = con.createStatement()) {
                stmt.execute("set transaction read only");
            }
        }
    }

    public static class DataSourceTransactionObject extends JdbcTransactionObjectSupport {
        private boolean newConnectionHolder;
        private boolean mustRestoreAutoCommit;

        public void setConnectionHolder(ConnectionHolder connectionHolder, boolean newConnectionHolder) {
            super.setConnectionHolder(connectionHolder);
            this.newConnectionHolder = newConnectionHolder;
        }
    }

}
