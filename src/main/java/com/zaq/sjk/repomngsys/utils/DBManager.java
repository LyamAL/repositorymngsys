package com.zaq.sjk.repomngsys.utils;

import java.sql.*;
import java.util.LinkedList;

/**
 * @author ZAQ
 */
public class DBManager {
    public static final int DEFAULT_CONNECTION_SIZE = 10;
    private final static String USERNAME = "sa";
    private final static String PASSWORD = "000000";
    private final static String URL = "jdbc:sqlserver://localhost:1433;database=repomngsys";
    private static DBManager dbManager;
    //创建一个连接池
    private static LinkedList<Connection> pool;

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
            pool = new LinkedList<>();
            try {
                for (int i = 0; i < DEFAULT_CONNECTION_SIZE; i++) {
                    //得到一个连接
                    Connection conn = getConnection();
                    pool.add(conn);
                }
            } catch (Exception e) {
                throw new ExceptionInInitializerError("数据库连接失败，请检查配置");
            }
        }
        return dbManager;
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        try {
            //1、加载驱动
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            //2、得到连接[指定连接到哪个数据库（clerk）]
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
            //clerk是数据库
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    //从池中获取一个连接
    public Connection getConnectionFromPool() {
        //移除一个连接对象
        return pool.removeFirst();
    }

    //释放资源
    public void release(Connection conn) {
        pool.addLast(conn);
    }

    public Statement createStatement(Connection conn) throws SQLException {
        Statement statement;
        if (conn != null) {
            statement = conn.createStatement();
        } else {
            throw new NullPointerException("Connection is null");
        }
        return statement;
    }

    public PreparedStatement prepareStatement(Connection connection, String sql) throws SQLException {
        PreparedStatement preparedStatement;
        if (connection != null) {
            preparedStatement = connection.prepareStatement(sql);
        } else {
            throw new NullPointerException("Connection is null");
        }
        return preparedStatement;
    }

    public ResultSet executeQuery(Statement statement, String sql) throws SQLException {
        ResultSet resultSet;
        if (statement != null) {
            resultSet = statement.executeQuery(sql);
        } else {
            throw new NullPointerException("Statement is null");
        }
        return resultSet;
    }

    public ResultSet query(Connection conn, String sql, Object[] args) throws SQLException {
        if (conn != null) {
            //有参数
            if (args != null) {
                PreparedStatement statement = this.prepareStatement(conn, sql);
                for (int i = 0; i < args.length; i++) {
                    statement.setObject(i + 1, args[i]);
                }
                return this.executeQuery(statement);
            }
            Statement stmt = this.createStatement(conn);
            return this.executeQuery(stmt, sql);
        }
        return null;
    }

    private ResultSet executeQuery(PreparedStatement statement) throws SQLException {
        ResultSet rs = null;
        if (statement != null) {
            statement.execute();
            rs = statement.getResultSet();
        }
        return rs;
    }

    public void close(Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
            statement = null;
        }
    }

    public void close(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            return;
        }
        resultSet.close();
        resultSet = null;
    }

    public int insert(Connection connection, String sql, Object[] args) {
        PreparedStatement preparedStatement = null;
        int res = 0;
        try {
            preparedStatement = this.prepareStatement(connection, sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            res = this.executeUpdate(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.close(preparedStatement);
            } catch (SQLException e) {
                preparedStatement = null;
            }
        }
        return res;
    }

    public int insertBatch(Connection connection, String sql, Object[][] args) {
        PreparedStatement preparedStatement = null;
        int res = 1;
        try {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            preparedStatement = this.prepareStatement(connection, sql);
            for (Object[] arg : args) {
                for (int j = 0; j < arg.length; j++) {
                    preparedStatement.setObject(j + 1, arg[j]);
                }
                preparedStatement.addBatch();
            }
            int[] counts = preparedStatement.executeBatch();
            for (int count : counts) {
                if (count == 0) {
                    connection.rollback();
                    res = 0;
                }
            }
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            e.printStackTrace();
            res = 0;
        } finally {
            try {
                this.close(preparedStatement);
            } catch (SQLException e) {
                preparedStatement = null;
            }
        }
        return res;
    }

    private int executeUpdate(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeUpdate();
    }

    public CallableStatement prepareCall(Connection conn, String sql) throws SQLException {
        if (conn == null) {
            return null;
        }
        return conn.prepareCall(sql);
    }


}
