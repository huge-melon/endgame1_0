package com.shixin.endgame.entity;

import java.util.ArrayList;
import java.util.List;

public class MapTable {
    private String dbType;
    private String dbName;
    private String tableName;
    private ArrayList<String> headMap;
    private ArrayList<String> endMap;

    public MapTable() {
    }

    public MapTable(String dbType, String dbName, String tableName, ArrayList<String> headMap, ArrayList<String> endMap) {
        this.dbType = dbType;
        this.dbName = dbName;
        this.tableName = tableName;
        this.headMap = headMap;
        this.endMap = endMap;
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

    public ArrayList<String> getHeadMap() {
        return headMap;
    }

    public void setHeadMap(ArrayList<String> headMap) {
        this.headMap = headMap;
    }

    public ArrayList<String> getEndMap() {
        return endMap;
    }

    public void setEndMap(ArrayList<String> endMap) {
        this.endMap = endMap;
    }

    @Override
    public String toString() {
        return "MapTable{" +
                "dbType='" + dbType + '\'' +
                ", dbName='" + dbName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", headMap=" + headMap +
                ", endMap=" + endMap +
                '}';
    }
}
