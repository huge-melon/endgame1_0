package com.shixin.endgame.service;

import com.shixin.endgame.config.MonogoConfig;
import com.shixin.endgame.config.MysqlConfig;
import com.shixin.endgame.config.OracleConfig;
import com.shixin.endgame.config.PostgreConfig;
import com.shixin.endgame.entity.DBinfo;
import com.shixin.endgame.dao.mysql.MysqlMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;

@Service

@org.springframework.context.annotation.Configuration
@MapperScan(basePackages = "com.userdefine.demo.dao.mysql",sqlSessionFactoryRef ="mysqlSqlSessionFactory" )
public class ConndbService {

    //添加数据库
    //删除数据库
    //根据名称返回对应的session

/*
    @Autowired
    SqlSessionFactory mysqlSqlSessionFactory;
*/

    private MysqlConfig mysqlConfig;
    private OracleConfig oracleConfig;
    private PostgreConfig postgreConfig;

    private SqlSessionFactory sqlSessionFactory;

    private MonogoConfig monogoConfig;

    private static HashMap<String,String> dbDriver = new HashMap<>() ;
    private static HashMap<String,SqlSessionFactory> dbSession=new HashMap<>();
    private static HashMap<String,MongoTemplate> mongoSession= new HashMap<>();

    public ConndbService() {
        dbDriver.put("MySQL","com.mysql.cj.jdbc.Driver");
        dbDriver.put("Oracle","oracle.jdbc.driver.OracleDriver");
    }


    public Object setDataSource(DBinfo dBinfo) throws Exception {


        System.out.println("setDataSource:  "+dBinfo.toString());
        //处理Mysql连接
        if(dBinfo.getDbType().equals("MySQL")){
            System.out.println(12345);
            mysqlConfig=new MysqlConfig(dBinfo);
            PooledDataSource dataSource=mysqlConfig.mysqlDataSource();
            sqlSessionFactory=mysqlConfig.sqlSessionFactory(dataSource);
            dbSession.put(dBinfo.getDbType()+dBinfo.getDbName(),sqlSessionFactory);
            return sqlSessionFactory;
        }
        else if(dBinfo.getDbType()=="Oracle"){
            oracleConfig=new OracleConfig(dBinfo);
        }
        else if(dBinfo.getDbType().equals("PostgreSQL")){
            System.out.println("Connect to PostgreSQL");
            postgreConfig = new PostgreConfig(dBinfo);
            PooledDataSource dataSource = postgreConfig.postgreDataSource();
            sqlSessionFactory = postgreConfig.sqlSessionFactory(dataSource);
            dbSession.put(dBinfo.getDbType()+dBinfo.getDbName(),sqlSessionFactory);
            return sqlSessionFactory;

        }
        else if(dBinfo.getDbType().equals("MongoDB")){
            System.out.println("mongoDB");
            monogoConfig = new MonogoConfig(dBinfo);
            MongoTemplate mongoTemplate = monogoConfig.mongoTemplate(monogoConfig.mongoDbFactory());
            mongoSession.put(dBinfo.getDbType()+dBinfo.getDbName(),mongoTemplate);
            return mongoTemplate;
        }

        System.out.println(dbSession.toString());
        return  null;
    }

    public SqlSessionFactory getSqlsessionFactory(String dbType,String dbName) {

        System.out.println("getTable: "+dbSession.get(dbType+dbName));
        return dbSession.get(dbType+dbName);
    }

    public Object getDbSessionMapping(String dbType,String dbName){
        return mongoSession.get(dbType+dbName);
    }
}
