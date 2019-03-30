package com.shixin.endgame.service;
//负责数据清洗

import com.shixin.endgame.dao.mysql.MysqlMapper;
import com.shixin.endgame.entity.ConditionTable;
import com.shixin.endgame.entity.MapTable;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CleanService {
    //去重
    //修改字段
    //删除缺失值
    //补全
    //采样
    public boolean delDuplicatedData(String dbType, String tableName, String columnsName,String id, SqlSessionFactory sqlSessionFactory){
        SqlSession session=sqlSessionFactory.openSession();
        if(dbType.equals("MySQL")) {
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
            String column = columnsName.split(",")[0];
            mysqlMapper.delDuplicatedData(tableName,columnsName,column,id);
            return true;
        }
        else if(dbType=="Oracle"){

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
        else if(dbType=="Oracle"){

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
}
