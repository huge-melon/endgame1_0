package com.shixin.endgame.service;

import com.shixin.endgame.config.MongoConfig;
import com.shixin.endgame.config.MysqlConfig;
import com.shixin.endgame.config.PostgreConfig;
import com.shixin.endgame.entity.DBinfo;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
//@MapperScan(basePackages = "com.userdefine.demo.dao.mysql",sqlSessionFactoryRef ="mysqlSqlSessionFactory" )
public class ConnectService {

    //添加数据库
    //删除数据库
    //根据名称返回对应的session

    /*
        @Autowired
        SqlSessionFactory mysqlSqlSessionFactory;
    */
    @Autowired
    private MysqlConfig mysqlConfig;
    @Autowired
    private PostgreConfig postgreConfig;
    @Autowired
    private MongoConfig mongoConfig;
    private SqlSessionFactory sqlSessionFactory;
    private static HashMap<String, Object> dbConnection = new HashMap<>();

    public ConnectService() {
    }

    public Object setDataSource(DBinfo dBinfo) throws Exception {
        System.out.println("setDataSource:  " + dBinfo.toString());
        //处理Mysql连接
        if (dBinfo.getDbType().equals("MySQL")) {
            System.out.println("Connect to MySQL: " + dBinfo.toString());
            sqlSessionFactory = mysqlConfig.mysqlSqlSessionFactory(dBinfo);
            dbConnection.put(dBinfo.getDbType() + dBinfo.getDbName(), sqlSessionFactory);
            return sqlSessionFactory;
        } else if (dBinfo.getDbType().equals("PostgreSQL")) {
            System.out.println("Connect to PostgreSQL: " + dBinfo.toString());
            sqlSessionFactory = postgreConfig.postgreSqlSessionFactory(dBinfo);
            dbConnection.put(dBinfo.getDbType() + dBinfo.getDbName(), sqlSessionFactory);
            return sqlSessionFactory;
        } else if (dBinfo.getDbType().equals("MongoDB")) {
            System.out.println("Connect to MongoDB: " + dBinfo.toString());
            MongoDbFactory mongoDbFactory = mongoConfig.mongoDbFactory(dBinfo);
            dbConnection.put(dBinfo.getDbType() + dBinfo.getDbName(), mongoDbFactory);
            return mongoDbFactory;
        }
        return null;
    }

    public Object getDateSource(String dbType, String dbName) {
        return dbConnection.get(dbType + dbName);
    }
}
