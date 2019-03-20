package com.shixin.endgame.service;

import com.shixin.endgame.dao.mysql.MysqlMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueryService {
    //@Autowired
    MysqlMapper mysqlMapper;
   // @Autowired @Qualifier("mysqlDataSource")
    PooledDataSource mysqlDataSource;

   // @Autowired @Qualifier("mysqlSqlSessionFactory")
    SqlSessionFactory mysqlSqlSessionFactory;

    //@Autowired @Qualifier("oracleSqlSessionFactory")
  //  SqlSessionFactory oralsqlSessionFactory;

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

     /**
      * 获取表中数据
      * @param tableName
      * @param dbType
      * @return
      */
     public List<Map<String,Object>> getTableData(String dbType ,String tableName,SqlSessionFactory sqlSessionFactory){
         SqlSession session=sqlSessionFactory.openSession();
         if(dbType.equals("MySQL")){
             MysqlMapper mysqlMapper= session.getMapper(MysqlMapper.class);
             return mysqlMapper.getTableData(tableName);
         }
         else if(dbType.equals("Oracle")){

         }
         else{
             System.out.println("error");
         }
         return null;
     }

     /**
     * 获取数据库中的表
     * @return
     */
     public List<Map<String,Object>> getTableName(String dbType, String dbName,SqlSessionFactory sqlSessionFactory){
         SqlSession session=sqlSessionFactory.openSession();
         if(dbType.equals("MySQL")) {
             MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
             return mysqlMapper.getTableName(dbName);
         }
         else if(dbType=="Oracle"){

         }
         else{
             System.out.println("error");
         }
         return null;
     }


    public List<Map<String,Object>> getTableMetaData(String dbType ,String dbName,String tableName,SqlSessionFactory sqlSessionFactory){
        SqlSession session=sqlSessionFactory.openSession();
        if(dbType.equals("MySQL")){
            MysqlMapper mysqlMapper= session.getMapper(MysqlMapper.class);
            return mysqlMapper.getTableMetaData(dbName,tableName);
        }
        else if(dbType.equals("Oracle")){

        }
        else{
            System.out.println("error");
        }
        return null;
    }
}