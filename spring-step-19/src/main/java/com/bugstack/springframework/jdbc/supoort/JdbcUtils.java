package com.bugstack.springframework.jdbc.supoort;

import cn.hutool.core.util.StrUtil;

import java.sql.*;

public class JdbcUtils {

    public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        String name = resultSetMetaData.getColumnLabel(columnIndex);
        if (StrUtil.isEmpty(name)) {
            name = resultSetMetaData.getColumnName(columnIndex);
        }
        return name;
    }


    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (null != obj) {
            className = obj.getClass().getName();
        }

        if (obj instanceof Blob) {
            Blob blob = (Blob) obj;
            obj = blob.getBytes(1, (int) blob.length());
        } else if (obj instanceof Clob) {
            Clob clob = (Clob) obj;
            obj = clob.getSubString(1, (int) clob.length());
        } else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
            obj = rs.getTimestamp(index);
        } else if (null != className && className.startsWith("oracle.sql.DATE")) {
            String metadataClassName = rs.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(metadataClassName) || "oracle.sql.TIMESTAMP".equals(metadataClassName)) {
                obj = rs.getTimestamp(index);
            } else {
                obj = rs.getDate(index);
            }
        } else if (obj instanceof Date) {
            if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
                obj = rs.getDate(index);
            }
        }

        return obj;
    }
}
