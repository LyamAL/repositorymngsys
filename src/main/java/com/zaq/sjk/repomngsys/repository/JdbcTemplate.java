package com.zaq.sjk.repomngsys.repository;

import com.zaq.sjk.repomngsys.exception.DuplicatePKException;
import com.zaq.sjk.repomngsys.utils.DBManager;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZAQ
 */
@Component
public class JdbcTemplate {

    private DBManager dbManager;

    public JdbcTemplate() {
        dbManager = DBManager.getInstance();
    }

    private DBManager getDbManager() {
        return dbManager;
    }


    private Map<String, List<Object>> convertToMap(ResultSet rs) {
        Map<String, List<Object>> map = new HashMap<>();
        ResultSetMetaData metaData;
        try {
            metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            //先生成几个list对象
            List<Object>[] columns = new ArrayList[count];

            for (int i = 0; i < columns.length; i++) {
                columns[i] = new ArrayList<>();
                map.put(metaData.getColumnName(i + 1), columns[i]);
            }
            /**
             * 这里是获取的一条一条
             */
            while (rs.next()) {
                for (int i = 0; i < columns.length; i++) {
                    columns[i].add(rs.getObject(i + 1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return map;
        }
        return map;

    }

    public Map<String, List<Object>> query(String sql, Object[] args) throws SQLException {
        Connection connection = this.dbManager.getConnectionFromPool();
        if (connection == null) {
            return new HashMap<>();
        }
        ResultSet rs = this.getDbManager().query(connection, sql, args);
        Map<String, List<Object>> result = convertToMap(rs);
        this.getDbManager().close(rs);
        this.getDbManager().release(connection);
        return result;
    }

    public int update(String sql, Object[] args) {
        Connection connection = this.dbManager.getConnectionFromPool();
        if (connection == null) {
            return 0;
        }
        int result = this.getDbManager().insert(connection, sql, args);
        this.getDbManager().release(connection);
        return result;
    }


    public void updateBatch(String sql, Object[][] args) {
        Connection connection = this.dbManager.getConnectionFromPool();
        if (connection == null) {
            return;
        }
        int result = this.getDbManager().insertBatch(connection, sql, args);
        this.getDbManager().release(connection);
    }

    public void close(ResultSet rs) {
        try {
            this.getDbManager().close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> call(String sql, Object[] args,
                                    Integer[] outputParamIdx, Integer[] outputParamTypes) throws DuplicatePKException {
        Connection conn = this.getDbManager().getConnectionFromPool();
        Map<String, Object> result = new HashMap<>();
        if (conn == null) {
            return result;
        }
        try {
            CallableStatement cstmt = this.getDbManager().prepareCall(conn, sql);
            for (int i = 0; i < args.length; i++) {
                cstmt.setObject(i + 1, args[i]);
            }
            if (outputParamIdx != null && outputParamIdx.length > 0) {
                if (outputParamTypes != null && outputParamTypes.length == outputParamIdx.length) {
                    for (int i = 0; i < outputParamIdx.length; i++) {
                        cstmt.registerOutParameter(outputParamIdx[i], outputParamTypes[i]);
                    }
                    cstmt.executeUpdate();
                    for (Integer paramIdx : outputParamIdx) {
                        Object o = cstmt.getObject(paramIdx);
                        result.put(String.valueOf(paramIdx), o);
                    }

                }
            } else {
                cstmt.executeUpdate();
                return result;
            }
            this.getDbManager().close(cstmt);
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("PRIMARY KEY")) {
                throw new DuplicatePKException();
            }
        } finally {
            this.getDbManager().release(conn);
        }

        return result;
    }


    public Map<String, List<Object>> call(String sql, Object[] args) {
        Connection conn = this.getDbManager().getConnectionFromPool();
        Map<String, List<Object>> result = new HashMap<>();
        if (conn == null) {
            return result;
        }
        try {
            CallableStatement cstmt = this.getDbManager().prepareCall(conn, sql);
            for (int i = 0; i < args.length; i++) {
                cstmt.setObject(i + 1, args[i]);
            }
            result = convertToMap(cstmt.executeQuery());
            this.getDbManager().close(cstmt);
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.getDbManager().release(conn);
        }

        return result;
    }

}
