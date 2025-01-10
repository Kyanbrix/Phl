package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFromPool {

    private final HikariDataSource dataSource;

    public ConnectionFromPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("");
        config.setUsername("");
        config.setPassword("");
        config.setMaximumPoolSize(1);
        config.setDriverClassName("");

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection() {
        dataSource.close();
    }




}
