package com.shixin.endgame.service;
//负责数据清洗

import com.shixin.endgame.dao.mysql.MysqlMapper;
import com.shixin.endgame.dao.postgresql.PostgresqlMapper;
import com.shixin.endgame.entity.ConditionTable;
import com.shixin.endgame.entity.MapTable;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class CleanService {
    //去重
    //修改字段
    //删除缺失值
    //补全
    //采样
    //Postgre没有合并
    public boolean delDuplicatedData(String dbType, String tableName, String columnsName,String id, SqlSessionFactory sqlSessionFactory){
        SqlSession session=sqlSessionFactory.openSession();
        if(dbType.equals("MySQL")) {
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
            String column = columnsName.split(",")[0];
            mysqlMapper.delDuplicatedData(tableName,columnsName,column,id);
            return true;
        }
        else if(dbType.equals("Oracle")){

        }
        else if(dbType.equals("PostgreSQL")){
            PostgresqlMapper postgresqlMapper=session.getMapper(PostgresqlMapper.class);
            tableName = "\""+tableName+"\"";
            id = "\""+id+"\"";
            String[] strs =columnsName.split(",");
            for(int i=0;i< strs.length;i++){
                strs[i]="\""+strs[i]+"\"";
            }
            String newColumnsName = StringUtils.join(strs,",");
            postgresqlMapper.delDuplicatedData(tableName,newColumnsName,strs[0],id);
            return true;
        }
        else{
            System.out.println("error");
        }
        return false;
    }

    public boolean delDataByNull(String dbType, String tableName, String columnsName,String method, SqlSessionFactory sqlSessionFactory){
        SqlSession session=sqlSessionFactory.openSession();
        if(dbType.equals("MySQL")) {
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
            List<String> column = Arrays.asList(columnsName.split(","));
            if(method.equals("and")) // 可用mybatis动态if
                mysqlMapper.delDataByNullAnd(tableName,column);
            else
                mysqlMapper.delDataByNullOr(tableName,column);
            return true;
        }
        else if(dbType.equals("Oracle")){

        }
        else if(dbType.equals("PostgreSQL")){
            PostgresqlMapper postgresqlMapper=session.getMapper(PostgresqlMapper.class);
            tableName = "\""+tableName+"\"";
            String[] strs =columnsName.split(",");
            for(int i=0;i< strs.length;i++){
                strs[i]="\""+strs[i]+"\"";
            }
            List<String> column = Arrays.asList(strs);
/*
            List<String> column = Arrays.asList(columnsName.split(","));
*/
            if(method.equals("and")) // 可用mybatis动态if
                postgresqlMapper.delDataByNullAnd(tableName,column);
            else
                postgresqlMapper.delDataByNullOr(tableName,column);
            return true;
        }
        else{
            System.out.println("error");
        }
        return false;
    }

    public boolean deleteTableColumn(String dbType,String tableName,String columnName,SqlSessionFactory sqlSessionFactory){
        SqlSession session=sqlSessionFactory.openSession();
        if(dbType.equals("MySQL")) {
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
            mysqlMapper.deleteTableColumn(tableName,columnName);
            return true;
        }
        else if(dbType=="Oracle"){

        }
        else{
            System.out.println("error");
        }
        return false;
    }

    public boolean deleteByCondition(ConditionTable conditionTable, SqlSessionFactory sqlSessionFactory){
        SqlSession session=sqlSessionFactory.openSession();
        if(conditionTable.getDbType().equals("MySQL")) {
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);

            System.out.println(conditionTable.getConditions().toString());
            mysqlMapper.deleteByCondition(conditionTable.getTableName(),conditionTable.getConditions());
          //  mysqlMapper.deleteByCondition();
            return true;
        }
        else if(conditionTable.getDbType().equals("Oracle")){

        }
        else{
            System.out.println("error");
        }
        return false;
    }

    //修改字段类型
    public boolean updateColumnType(String dbType, String tableName, String column, String oldType, String newType, SqlSessionFactory sqlSessionFactory){
        SqlSession session=sqlSessionFactory.openSession();
        if (dbType.equals("MySQL")){
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
            /*
            *
            * 将所有的值进行类型检查
            *
            * 将oldType做对照
            * */
            mysqlMapper.updateColumnType(tableName,column,newType);
            return true;

        }
        else if(dbType.equals("PostgreSQL")){
            PostgresqlMapper postgresqlMapper=session.getMapper(PostgresqlMapper.class);

        }
        else if(dbType.equals("MongoDB")){

        }else{
            System.out.println("error");
        }
        return false;
    }

    //剪切字符串
    public boolean cutString(String dbType, String tableName, String columnName, String priKey,String opType,String beginKey,String endKey,SqlSessionFactory sqlSessionFactory){
        SqlSession session = sqlSessionFactory.openSession();
        if (dbType.equals("MySQL")){
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
            List<Map<String,Object>> columnData= mysqlMapper.getColumnData(tableName,columnName,priKey);
            if(opType.equals("pos")){
                for (Map<String,Object> mo:columnData) {
                    if(mo.containsKey(columnName)){
                        String str = (String)mo.get(columnName);
                        int begin = Integer.parseInt(beginKey)>str.length()?0:Integer.parseInt(beginKey);
                        int end = Integer.parseInt(endKey)>str.length()?str.length():Integer.parseInt(endKey);
                        mo.replace(columnName,str.substring(begin, end));
                    }
                }
            }
            else{
                for (Map<String,Object> mo:columnData) {
                    if(mo.containsKey(columnName)){
                        String str = (String)mo.get(columnName);
                        int begin = str.indexOf(beginKey)==-1?0:str.indexOf(beginKey);
                        int end = str.lastIndexOf(endKey)==-1?str.length():str.indexOf(endKey);
                        mo.replace(columnName,str.substring(begin, end));
                    }
                }
            }
            for (Map<String,Object> mo:columnData) {
                if(mo.containsKey(columnName)) {
                    mysqlMapper.setColumnData(tableName, columnName, (String) mo.get(columnName), priKey, mo.get(priKey).toString());
                }
            }
            return true;
        }
        else if(dbType.equals("PostgreSQL")){
            PostgresqlMapper postgresqlMapper=session.getMapper(PostgresqlMapper.class);

        }
        else if(dbType.equals("MongoDB")){

        }else{
            System.out.println("error");
        }
        return false;
    }

    //字段补全
    public boolean completFiled(String dbType, String tableName, String columnName, String completType, String defaultValue, SqlSessionFactory sqlSessionFactory) {
        SqlSession session = sqlSessionFactory.openSession();
        Double compleValue;
        if (dbType.equals("MySQL")){
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
            if(completType.equals("average")){
                compleValue=mysqlMapper.getAverage(tableName,columnName);
                System.out.println("average:  " + compleValue);

            }else if(completType.equals("mode")){
                List<Map<String,Object>> mp =mysqlMapper.getMode(tableName,columnName);
                System.out.println("mode:  " + mp.toString());
                compleValue=Double.parseDouble(mp.get(0).get(columnName).toString());

            }else if(completType.equals("median")){
                List<Double> number = mysqlMapper.getColumnDouble(tableName,columnName);
                System.out.println("source:  "+number);
                System.out.println("source:  "+number.size());
                while (number.contains(null)){
                    number.remove(null);
                }
                Collections.sort(number);
                System.out.println("target:  "+number);
                System.out.println("target:  "+number.size());

                int size = number.size();
                if(size%2==0){
                    System.out.println("ou数个：  " + size/2);
                    compleValue = (number.get((size>>1))+number.get(((size>>1)-1)))/2;
                }
                else{
                    System.out.println("奇数个：  " + size/2);
                    compleValue = number.get(size>>1);
                }
                System.out.println("median:  " + compleValue);
            }else {
                compleValue=Double.parseDouble(defaultValue);
                System.out.println("customize:  " + compleValue);
            }
            mysqlMapper.completFiled(tableName,columnName,compleValue);
            return true;
        }
        else if(dbType.equals("PostgreSQL")){
            PostgresqlMapper postgresqlMapper=session.getMapper(PostgresqlMapper.class);

        }
        else if(dbType.equals("MongoDB")){

        }else{
            System.out.println("error");
        }
        return false;
    }

    public List<Map<String,Object>> dataVerify(String dbType, String tableName, String columnName,String priKey ,String regularExpress,SqlSessionFactory sqlSessionFactory){
        SqlSession session = sqlSessionFactory.openSession();
        if (dbType.equals("MySQL")){
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);

            List<Map<String,Object>> origin = mysqlMapper.getColumnData(tableName,columnName,priKey);
            List<Map<String,Object>> notMatchList = new ArrayList<>();
            System.out.println(regularExpress);
            for (Map<String,Object> o: origin) {
                System.out.println("子元素： " + o);
                if(o.get(columnName)==null){

                    System.out.println("为空：  "+o.get(priKey));
                    o.put(columnName,"null");
                    notMatchList.add(o);
                }else if(!Pattern.matches(regularExpress,(String)o.get(columnName))){
                    System.out.println("不匹配：  "+o.get(priKey)+"   "+o.get(columnName));
                    notMatchList.add(o);
                }
            }
            System.out.println(notMatchList);
            return notMatchList;

        }
        else if(dbType.equals("PostgreSQL")){
            PostgresqlMapper postgresqlMapper=session.getMapper(PostgresqlMapper.class);

        }
        else if(dbType.equals("MongoDB")){

        }else{
            System.out.println("error");
        }
        return null;
    }

    public boolean saveUpdateDate(String dbType, String tableName, String columnName, String priKey, List<Map<String, Object>> data, SqlSessionFactory sqlSessionFactory) {
        SqlSession session = sqlSessionFactory.openSession();
        if (dbType.equals("MySQL")){
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
            for (Map<String,Object> mp : data) {
                if(mp.get("targetdata")!=null){
                    mysqlMapper.setColumnData(tableName,columnName,mp.get("targetdata").toString(),priKey,mp.get("id").toString());
                }
            }
            return true;
        }
        else if(dbType.equals("PostgreSQL")){
            PostgresqlMapper postgresqlMapper=session.getMapper(PostgresqlMapper.class);
        }
        else if(dbType.equals("MongoDB")){

        }else{
            System.out.println("error");
        }
        return false;

    }

    public boolean rdbToRdb(boolean userDefine, String sourceDbType, String sourceDbName, String sourceTableName, List<String> sourceColumnList,
                            String targetDbType, String targetDbName, String targetTableName, List<String> targetColumnList, SqlSessionFactory sourceSqlSessionFactory, SqlSessionFactory targetSqlSessionFactory) {
        SqlSession sourceSession = sourceSqlSessionFactory.openSession();
        SqlSession targetSession = targetSqlSessionFactory.openSession();

        MysqlMapper sourceMysqlMapper=null;
        PostgresqlMapper sourcePostgresqlMapper=null;
        MysqlMapper targetMysqlMapper=null;
        PostgresqlMapper targetPostgresqlMapper=null;
        String sourceColumns=sourceColumnList.toString();
        String targetColumns=targetColumnList.toString();
        List<Map<String,Object>> sourceData =null;

        //源数据库提取数据
        if(sourceDbType.equals("MySQL")){
            sourceMysqlMapper=sourceSession.getMapper(MysqlMapper.class);
            sourceData=sourceMysqlMapper.getUserDefineData(sourceTableName,sourceColumns.substring(1,sourceColumns.length()-1));
        }else if(sourceDbType.equals("PostgreSQL")){
            sourcePostgresqlMapper=sourceSession.getMapper(PostgresqlMapper.class);
        }else{
            System.out.println("source error");
            return false;
        }
        //向目标数据库中插入数据
        if(targetDbType.equals("MySQL")){
            targetMysqlMapper=targetSession.getMapper(MysqlMapper.class);
            for (Map<String,Object> mp :sourceData  ) {
                String targetData="";
                for (String col: sourceColumnList) {
                    targetData+="'"+mp.get(col)+"',";
                }
                targetData=targetData.substring(0,targetData.length()-1);
                targetMysqlMapper.insertData(targetTableName,targetColumns.substring(1,targetColumns.length()-1),targetData);
            }
            return true;
        }else if(targetDbType.equals("PostgreSQL")){
            targetPostgresqlMapper=sourceSession.getMapper(PostgresqlMapper.class);
        }else{
            System.out.println("target error");
            return false;
        }

        if(!userDefine){

        }
        else{

        }
        return false;
    }

    public List<Map<String, Object>> replaceString(String dbType, String tableName, String columnName, String priKey, String regularExpress, String targetString, SqlSessionFactory sqlSessionFactory) {
        SqlSession session = sqlSessionFactory.openSession();
        if (dbType.equals("MySQL")){
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
            //原始数据
            List<Map<String,Object>> origin = mysqlMapper.getColumnData(tableName,columnName,priKey);

            System.out.println(regularExpress);
            for (Map<String,Object> o: origin) {
                System.out.println("子元素： " + o);
                if(o.get(columnName)==null){
                    System.out.println("为空：  "+o.get(priKey));
                    o.put(columnName,"null");
                }else {
                    String replace = o.get(columnName).toString().replaceAll(regularExpress,targetString);
                    o.put("afterReplace",replace);
                }
            }
            System.out.println(origin);
            return origin;

        }
        else if(dbType.equals("PostgreSQL")){
            PostgresqlMapper postgresqlMapper=session.getMapper(PostgresqlMapper.class);

        }
        else if(dbType.equals("MongoDB")){

        }else{
            System.out.println("error");
        }
        return null;

    }
}
