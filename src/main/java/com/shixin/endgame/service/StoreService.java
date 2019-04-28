package com.shixin.endgame.service;

import com.shixin.endgame.dao.mongodb.MongoDao;
import com.shixin.endgame.dao.mysql.MysqlMapper;
import com.shixin.endgame.dao.postgresql.PostgresqlMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

//存储
@Service
public class StoreService {
    private MysqlMapper mysqlMapper;
    private PostgresqlMapper postgresqlMapper;

    public boolean rdbToRdb(String sourceDbType, String sourceTableName, List<String> sourceColumnList,
                            String targetDbType, String targetTableName, List<String> targetColumnList, SqlSessionFactory sourceSqlSessionFactory, SqlSessionFactory targetSqlSessionFactory) {
        SqlSession sourceSession = sourceSqlSessionFactory.openSession();
        SqlSession targetSession = targetSqlSessionFactory.openSession();

        List<Map<String, Object>> sourceData = null;
        //源数据库提取数据
        if (sourceDbType.equals("MySQL")) {
            mysqlMapper = sourceSession.getMapper(MysqlMapper.class);
            sourceData = mysqlMapper.getUserDefineData(sourceTableName, sourceColumnList);
        } else if (sourceDbType.equals("PostgreSQL")) {
            postgresqlMapper = sourceSession.getMapper(PostgresqlMapper.class);
            sourceData = postgresqlMapper.getUserDefineData(sourceTableName, sourceColumnList);
        } else {
            System.out.println("source error");
            return false;
        }
        //向目标数据库中插入数据
        if (targetDbType.equals("MySQL")) {
            mysqlMapper = targetSession.getMapper(MysqlMapper.class);
            for (Map<String, Object> mp : sourceData) {
                String targetData = "";
                for (String col : sourceColumnList) {
                    targetData += "'" + mp.get(col) + "',";
                }
                targetData = targetData.substring(0, targetData.length() - 1);
                System.out.println("targetData:  " + targetData);
                mysqlMapper.insertData(targetTableName, targetColumnList, targetData);
            }
            return true;
        } else if (targetDbType.equals("PostgreSQL")) {
            postgresqlMapper = targetSession.getMapper(PostgresqlMapper.class);
            for (Map<String, Object> mp : sourceData) {
                String targetData = "";
                for (String col : sourceColumnList) {
                    targetData += "'" + mp.get(col) + "',";
                }
                targetData = targetData.substring(0, targetData.length() - 1);
                postgresqlMapper.insertData(targetTableName, targetColumnList, targetData);
            }
            return true;
        } else {
            System.out.println("target error");
        }
        return false;
    }

    public boolean rdbToMongo(String sourceDbType, String sourceTableName, List<String> sourceColumnList, String targetCollectionName, SqlSessionFactory sqlSessionFactory, MongoDbFactory mongoDbFactory) {
        SqlSession session = sqlSessionFactory.openSession();
        List<Map<String, Object>> sourceData = null;
        if (sourceDbType.equals("MySQL")) {
            mysqlMapper = session.getMapper(MysqlMapper.class);
            sourceData = mysqlMapper.getUserDefineData(sourceTableName, sourceColumnList);

        } else if (sourceDbType.equals("PostgreSQL")) {
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            sourceData = postgresqlMapper.getUserDefineData(sourceTableName, sourceColumnList);
        } else {
            System.out.println("error");
            return false;
        }

        MongoDao mongoDao = new MongoDao(mongoDbFactory);
        mongoDao.insertData(targetCollectionName, sourceData);

        return true;


    }

    public boolean mongoToMongo() {
        return true;
    }

    //数据转换

}
