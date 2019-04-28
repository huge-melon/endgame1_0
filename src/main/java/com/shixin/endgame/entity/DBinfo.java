package com.shixin.endgame.entity;

public class DBinfo {
    private String dbType;
    private String dbUrl;
    private String dbPort;
    private String dbName;
    private String userName;
    private String userPassword;

    public DBinfo() {
    }

    public DBinfo(String dbType, String dbUrl, String dbPort, String dbName, String userName, String userPassword) {
        this.dbType = dbType;
        this.dbUrl = dbUrl;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "DBinfo{" +
                "dbType='" + dbType + '\'' +
                ", dbUrl='" + dbUrl + '\'' +
                ", dbPort='" + dbPort + '\'' +
                ", dbName='" + dbName + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
