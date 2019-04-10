package com.shixin.endgame.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static void main(String[] args){

       /* String content ="I am noob " +
                "from runoob.com.";
        String pattern1 = ".*runo2ob.*";
        System.out.println(Pattern.matches(pattern1,content));

        // 按指定模式在字符串查找
        String line = "This order was placed for QT3000! OK?";
        String pattern = "(\\D*)(\\d+)(.*)";

        Pattern r = Pattern.compile(pattern);
        Matcher m =r.matcher(line);
        if(m.find()){
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
            System.out.println("Found value: " + m.group(3) );
        }
        else{
            System.out.println("NO MATCH");
        }
*/

        List<Map<String,String>> origin =new ArrayList<>();
        for(int i=0;i<20;i++){
            HashMap<String,String> o = new HashMap<>();
            o.put(String.valueOf(i),String.valueOf(i));
            origin.add(o);
        }
        System.out.println(origin);

        for(Map<String,String> mp : origin){
            mp.put("b","233");
        }
        System.out.println(origin);



        return;
    }
}
