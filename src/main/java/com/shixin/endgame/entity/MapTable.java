package com.shixin.endgame.entity;

import com.mongodb.MongoClientURI;
import com.shixin.endgame.dao.mongodb.MongoDao;
import com.shixin.endgame.service.ConndbService;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void main(String[] args) throws Exception {
        ConndbService conndbService = new ConndbService();
        DBinfo dBinfo = new DBinfo();
        dBinfo.setDbType("MongoDB");
        dBinfo.setDbName("runoob");
        dBinfo.setDbUrl("localhost");
        dBinfo.setDbPort("27017");
        dBinfo.setUserName("root");

        List<String> stringList = new ArrayList<>();
        stringList.add("userName");
        stringList.add("class");
        stringList.add("address");
       // mongoDao.delDuplicatedData("user",stringList);

        //mongoDao.delDataByNull("user","and",stringList);
//        mongoDao.deleteKey("user","user_sex");
        //mongoDao.deleteByCondition();
       // mongoDao.updateColumnType();

        String url = "mongodb://" +dBinfo.getDbUrl() + "/" +dBinfo.getDbName(); //+ dBinfo.getUserName() + ":" + dBinfo.getUserPassword()+ "@"
        System.out.println("MongoDB" + url);
        MongoClientURI mongoClientURI = new MongoClientURI(url);
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClientURI);
        MongoDao mongoDao = new MongoDao(mongoDbFactory);
      //  mongoDao.cutString();

/*        String str = "class,username,sex";
        List<String> list = Arrays.asList(str);
        System.out.println("111:  "+ list);*/
        return;
    }
}
