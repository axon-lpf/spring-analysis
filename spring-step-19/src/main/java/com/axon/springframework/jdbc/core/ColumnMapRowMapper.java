package com.axon.springframework.jdbc.core;

import com.axon.springframework.jdbc.supoort.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ColumnMapRowMapper implements RowMapper<Map<String, Object>> {

    /**
     * 转换成行对象
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columnCount = rsMetaData.getColumnCount();
        Map<String, Object> mapOfColumnValues = createColumnMap(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            String columnName = JdbcUtils.lookupColumnName(rsMetaData, i);
            mapOfColumnValues.putIfAbsent(getColumnKey(columnName), getColumnValue(rs, i));
        }
        return mapOfColumnValues;
    }

    protected Map<String, Object> createColumnMap(int columnCount) {
        return new LinkedHashMap<>(columnCount);
    }

    protected String getColumnKey(String columnName) {
        return columnName;
    }

    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index);
    }
}
