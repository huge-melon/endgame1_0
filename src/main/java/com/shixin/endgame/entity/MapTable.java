package com.shixin.endgame.entity;

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

      /*  SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MMM-dd HH:mm:ss:SSS");
        String formatStr =formatter.format(new Date());
        System.out.println(formatStr);//2017-九月-15 13:17:08:355

        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM -dd HH:mm:ss:SSS");
        String formatStr2 =formatter2.format(new Date());
        System.out.println(formatStr2);//2017-09-15 13:18:44:672*/

        List<String> condtionList = new ArrayList<>();
        condtionList.add("aaa");
        condtionList.add("bbb");
        condtionList.add("ccc");
        condtionList.add("ddd");
        condtionList.add("eee");
        condtionList.add("fff");
        condtionList.add("ggg");
        List<String> afterModify = new ArrayList<>();
        for (String  con: condtionList) {
            con = con+"111";

            //afterModify.add(con);
        }
        System.out.println(condtionList);

        String n = "0123456789";
        int pos = n.indexOf('5');
        System.out.println("pos:  "+ pos);
        System.out.println(n.substring(0,pos));
        System.out.println(n.substring(pos,n.length()));

        return;
    }
}
