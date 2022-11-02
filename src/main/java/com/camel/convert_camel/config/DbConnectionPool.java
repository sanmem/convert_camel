package com.camel.convert_camel.config;

import com.camel.convert_camel.domain.DbStatus;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConnectionPool {

    public DataSource getDataSource(DbStatus dbStatus)
    {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(dbStatus.getDbDriver());
        dataSourceBuilder.url(dbStatus.getDbJdbcUrl());
        dataSourceBuilder.username(dbStatus.getDbUserName());
        dataSourceBuilder.password(dbStatus.getDbPassword());
        return dataSourceBuilder.build();
    }

    public Connection getConnection(DbStatus dbStatus){
        try {
            return getDataSource(dbStatus).getConnection();
        } catch (SQLException e) {
            return null;
        }
    }
}
