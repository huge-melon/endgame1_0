package com.shixin.endgame.entity;

import java.util.ArrayList;

public class ConditionTable {
    private String dbType;
    private String dbName;
    private String tableName;
    private ArrayList<String> conditions;

    public ConditionTable() {
    }

    public ConditionTable(String dbType, String dbName, String tableName, ArrayList<String> conditions) {
        this.dbType = dbType;
        this.dbName = dbName;
        this.tableName = tableName;
        this.conditions = conditions;
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

    public ArrayList<String> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<String> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "ConditionTable{" +
                "dbType='" + dbType + '\'' +
                ", dbName='" + dbName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", conditions=" + conditions +
                '}';
    }
}
