package com.shixin.endgame.service;

import com.shixin.endgame.dao.mongodb.MongoDao;
import com.shixin.endgame.dao.mysql.MysqlMapper;
import com.shixin.endgame.dao.postgresql.PostgresqlMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * 获取数据库中的表
     * @return
     */
    public List<Map<String,Object>> getTableName(String dbType, String dbName,Object dbConnect){

        if(dbType.equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory)dbConnect;
            SqlSession session=sqlSessionFactory.openSession();
            MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
            return mysqlMapper.getTableName(dbName);
        }
        else if(dbType.equals("PostgreSQL")){
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory)dbConnect;
            SqlSession session=sqlSessionFactory.openSession();
            PostgresqlMapper postgresqlMapper =session.getMapper(PostgresqlMapper.class);
            return postgresqlMapper.getTableName(dbName);

        }
        else if(dbType.equals("MongoDB")){
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            MongoDao mongoDao = new MongoDao(mongoDbFactory);

            Set colsName = mongoDao.getTableName();
            List<Map<String,Object>> collections = new ArrayList<>();
            for (Object col: colsName) {
                Map<String,Object> table =new HashMap<>();
                table.put("TABLE_NAME",col);
                collections.add(table);
            }
            return collections;
        }
        else{
            System.out.println("error");
        }
        return null;
    }

    /**
      * 获取表中数据
      * @param tableName
      * @param dbType
      * @return
      */
     public List<Map<String,Object>> getTableData(String dbType ,String tableName,Object dbConnect){

         if(dbType.equals("MySQL")){
             SqlSessionFactory sqlSessionFactory = (SqlSessionFactory)dbConnect;
             SqlSession session=sqlSessionFactory.openSession();
             MysqlMapper mysqlMapper= session.getMapper(MysqlMapper.class);
             return mysqlMapper.getTableData(tableName);
         }
         else if(dbType.equals("PostgreSQL")){
             System.out.println("获取表中数据");
             SqlSessionFactory sqlSessionFactory = (SqlSessionFactory)dbConnect;
             SqlSession session=sqlSessionFactory.openSession();
             PostgresqlMapper postgresqlMapper =session.getMapper(PostgresqlMapper.class);
             return postgresqlMapper.getTableData(tableName);
         }
         else if(dbType.equals("MongoDB")){
             System.out.println("MongoDB 获取表中数据");

             MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
             MongoDao mongoDao = new MongoDao(mongoDbFactory);
             List data = mongoDao.findMongoData(tableName);
             for(Iterator it = data.iterator();it.hasNext();){
                 HashMap<String,Object> o =(HashMap<String, Object>) it.next();
                 String id= o.get("_id").toString();
                 o.replace("_id",id);
             }
             return data;
         }
         else{
             System.out.println("error");
         }
         return null;
     }


     //获取表中的元数据
     public List<Map<String,Object>> getTableMetaData(String dbType ,String dbName,String tableName,Object dbConnect){
         if(dbType.equals("MySQL")){
             SqlSessionFactory sqlSessionFactory = (SqlSessionFactory)dbConnect;
             SqlSession session=sqlSessionFactory.openSession();
            MysqlMapper mysqlMapper= session.getMapper(MysqlMapper.class);
            return mysqlMapper.getTableMetaData(dbName,tableName);
        }
        else if(dbType.equals("PostgreSQL")){
             SqlSessionFactory sqlSessionFactory = (SqlSessionFactory)dbConnect;
             SqlSession session=sqlSessionFactory.openSession();
            PostgresqlMapper postgresqlMapper =session.getMapper(PostgresqlMapper.class);
            List<Map<String,Object>> temp = postgresqlMapper.getTableMetaData(tableName);
            List<Map<String,Object>> prikey = postgresqlMapper.getPriKey(tableName);
            Set<Object> sob = new HashSet<>();
            for (Map<String,Object> p: prikey) {
                sob.add(p.get("COLUMN_KEY"));
            }
            System.out.println(sob.toString());
            System.out.println(sob.contains("id"));
            for (Map<String,Object> hp: temp) {
                System.out.println(sob.contains(hp.get("COLUMN_NAME")));
                if((boolean)hp.get("IS_NULLABLE")){
                    hp.replace("IS_NULLABLE","NO");
                }
                else{
                    hp.replace("IS_NULLABLE","YES");
                }
                if(!hp.containsKey("COLUMN_COMMENT")){
                    hp.put("COLUMN_COMMENT","");
                }
                if(sob.contains(hp.get("COLUMN_NAME"))){
                    hp.put("COLUMN_KEY","PRI");
                }
                else {
                    hp.put("COLUMN_KEY","");
                }
            }
            return temp;
        }
        else if(dbType.equals("MongoDB")){
             MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
             MongoDao mongoDao = new MongoDao(mongoDbFactory);
             return mongoDao.getMetaData(tableName);
        }
        else{
            System.out.println("error");
        }
        return null;
    }


    public List<Map<String,Object>> searchByCondition(String collectionName, String key_field, String operation, String keyWord, Object dbConnect) {

        MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
        MongoDao mongoDao = new MongoDao(mongoDbFactory);
        List data = mongoDao.searchByCondition(collectionName,key_field,operation,keyWord);
        for(Iterator it = data.iterator();it.hasNext();){
            HashMap<String,Object> o =(HashMap<String, Object>) it.next();
            String id= o.get("_id").toString();
            o.replace("_id",id);
        }
        return data;
    }
}