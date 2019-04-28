package com.shixin.endgame.entity;

public class RegularExpress {
    private String dbType;
    private String dbName;
    private String tableName;
    private String columnName;
    private String priKey;
    private String regularExpress;
    private String targetString;

    public RegularExpress() {
    }

    public RegularExpress(String dbType, String dbName, String tableName, String columnName, String priKey, String regularExpress, String targetString) {
        this.dbType = dbType;
        this.dbName = dbName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.priKey = priKey;
        this.regularExpress = regularExpress;
        this.targetString = targetString;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public String getRegularExpress() {
        return regularExpress;
    }

    public void setRegularExpress(String regularExpress) {
        this.regularExpress = regularExpress;
    }

    public String getTargetString() {
        return targetString;
    }

    public void setTargetString(String targetString) {
        this.targetString = targetString;
    }

    @Override
    public String toString() {
        return "RegularExpress{" +
                "dbType='" + dbType + '\'' +
                ", dbName='" + dbName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", priKey='" + priKey + '\'' +
                ", regularExpress='" + regularExpress + '\'' +
                ", targetString='" + targetString + '\'' +
                '}';
    }
}
