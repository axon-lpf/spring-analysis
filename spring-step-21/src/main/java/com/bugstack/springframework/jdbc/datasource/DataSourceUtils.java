package com.bugstack.springframework.jdbc.datasource;

import com.bugstack.springframework.jdbc.CannotGetJdbcConnectionException;
import com.bugstack.springframework.tx.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DataSourceUtils {

    /**
     *  获取链接是事务的核心机制， 原来是执行每条sql都会获取一个新的链接，
     *  现在是在当前线程中操作的都去使用同一个链接，这样保证事务的回滚
     *
     *  如果这里每次都返回一个新的链接，就无法回滚，每次新的链接就相当于一个新的事物。   所以保证在同一个链接下，去提交多条执行sql语句。
     *  下面获取的链接的方式则不行，每次都是新的链接，错误案例
     *      public static Connection getConnection(DataSource dataSource) {
     *         try {
     *             return dataSource.getConnection();
     *         } catch (SQLException e) {
     *             throw new RuntimeException("Failed to obtain JDBC Connection", e);
     *         }
     *     }
     *
     * @param dataSource
     * @return
     */
    public static Connection getConnection(DataSource dataSource) {
        try {
            return doGetConnection(dataSource);
        } catch (SQLException e) {
            throw new CannotGetJdbcConnectionException("Failed to obtain JDBC Connection", e);
        }
    }


    public static Connection doGetConnection(DataSource dataSource) throws SQLException {
        ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        if (null != conHolder && conHolder.hasConnection()) {
            return conHolder.getConnection();
        }
        return fetchConnection(dataSource);
    }

    private static Connection fetchConnection(DataSource dataSource) throws SQLException {
        Connection conn = dataSource.getConnection();
        if (null == conn) {
            throw new IllegalArgumentException("DataSource return null from getConnection():" + dataSource);
        }
        return conn;
    }

    public static void releaseConnection(Connection con, DataSource dataSource) {
        try {
            doReleaseConnection(con, dataSource);
        } catch (Exception ignore) {}
    }

    public static void doReleaseConnection(Connection con, DataSource dataSource) throws SQLException {
        if (con == null) {
            return;
        }
        doCloseConnection(con, dataSource);
    }

    public static void doCloseConnection(Connection con, DataSource dataSource) throws SQLException {
        con.close();
    }

}
