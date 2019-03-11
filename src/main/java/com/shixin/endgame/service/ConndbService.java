package com.shixin.endgame.service;

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
import org.springframework.context.annotation.Bean;
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

    private HashMap<String,String> dbDriver ;

    public ConndbService(){
        dbDriver=new HashMap<>();
        dbDriver.put("MySQL","com.mysql.cj.jdbc.Driver");
    }



    //将从页面上读取的DBinfo，装配到数据库Mysqlconfig中
    写法有问题，自己注入自己
    @Bean(name = "DBinfo")
    public DBinfo setDbinfo(DBinfo dBinfo){
        return dBinfo;
    }



    public SqlSessionFactory setDataSource(DBinfo dBinfo){

        String url="jdbc:mysql://"+dBinfo.getDbUrl()+':'+dBinfo.getDbPort()+'/'+dBinfo.getDbName();
        //修改地方，添加连接属性
        String conf="?serverTimezone=GMT%2B8";
        //构建数据库连接池
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver(dbDriver.get(dBinfo.getDbType()));
        dataSource.setUrl(url+conf);
        dataSource.setUsername(dBinfo.getUserName());
        dataSource.setPassword(dBinfo.getUserPassword());
        //构建数据库事务方式
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        //创建数据库运行环境
        Environment environment= new Environment("development",transactionFactory,dataSource);

        //构建Configuration对象
        Configuration configuration = new Configuration(environment);

        //注册一个MyBatis上下文别名
        //111111
        //加入一个映射器

        configuration.addMapper(MysqlMapper.class);

        //使用SqlSessionFactoryBuilder构建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);


        try {
            System.out.println(dataSource.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  sqlSessionFactory;

    }


}
