package com.alfy.rest.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Author: Alan Created: 12/13/2014.
 */
public class ConnectionPool {
  private static final String DB_USERNAME = System.getProperty("DB_USERNAME");
  private static final String DB_PASSWORD = System.getProperty("DB_PASSWORD");
  private static final String DB_DATABASE = System.getProperty("DB_DATABASE");
  private static final String DB_DRIVER = System.getProperty("DB_DRIVER");
  private static final String DB_HOST = System.getProperty("DB_HOST");
  private static final String DB_PORT = System.getProperty("DB_PORT");
  private static final String DB_NAME = System.getProperty("DB_NAME");
  private static final String DB_URL = "jdbc:" + DB_DATABASE + "://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

  //// Singleton ////
  private static ConnectionPool instance;

  public static ConnectionPool getInstance() {
    if (instance == null) {
      instance = new ConnectionPool();
    }
    return instance;
  }
  //// Singleton ////

  private BasicDataSource basicDataSource;

  private ConnectionPool() {
    basicDataSource = new BasicDataSource();
    basicDataSource.setDriverClassName(DB_DRIVER);

    basicDataSource.setUsername(DB_USERNAME);
    basicDataSource.setPassword(DB_PASSWORD);
    basicDataSource.setUrl(DB_URL);
    basicDataSource.setInitialSize(1);
  }

  public Connection getConnection() throws SQLException {
    return basicDataSource.getConnection();
  }
}