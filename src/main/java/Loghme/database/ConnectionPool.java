package Loghme.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static ComboPooledDataSource connectionPool = new ComboPooledDataSource();

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        connectionPool.setJdbcUrl("jdbc:mysql://localhost:3306/Loghme?useSSL=false&useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        connectionPool.setUser("ie");
        connectionPool.setPassword("iePass@2020");

        connectionPool.setInitialPoolSize(5);
        connectionPool.setMinPoolSize(5);
        connectionPool.setAcquireIncrement(5);
        connectionPool.setMaxPoolSize(20);
        connectionPool.setMaxStatements(100);

    }

    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    private ConnectionPool() {}

}

