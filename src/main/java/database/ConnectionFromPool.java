package database;

import config.Discord;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFromPool {

    private final HikariDataSource dataSource;

    public ConnectionFromPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Discord.url());
        config.setUsername(Discord.username());
        config.setPassword(Discord.password());
        config.setMaximumPoolSize(6);

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection() {
        dataSource.close();
    }




}
