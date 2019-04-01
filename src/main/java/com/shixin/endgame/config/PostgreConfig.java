package com.shixin.endgame.config;


import com.shixin.endgame.entity.DBinfo;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.activation.DataSource;
import java.io.IOException;
import java.util.Properties;

/*
@Configuration*/
@MapperScan(basePackages = "com.shixin.endgame.dao.mysql",sqlSessionFactoryRef = "mysqlSqlSessionFactory")

public class PostgreConfig {

    private DBinfo dBinfo;
    //private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PostgreConfig(){

    }
    public PostgreConfig(DBinfo db){
        dBinfo=db;
        System.out.println("Const:  "+dBinfo.toString());

    }

    /*
        @Bean(name = "postgreDataSource")
    */
    public PooledDataSource postgreDataSource() {

        System.out.println("dataSource:  "+dBinfo.toString());
        String url = "jdbc:postgresql://" + dBinfo.getDbUrl() + ':' + dBinfo.getDbPort() + '/' + dBinfo.getDbName();
        //修改地方，添加连接属性
        String conf = "?serverTimezone=GMT%2B8";
        //构建数据库连接池
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("org.postgresql.Driver");
        dataSource.setUrl(url + conf);
        dataSource.setUsername(dBinfo.getUserName());
        dataSource.setPassword(dBinfo.getUserPassword());
        return dataSource;
    }


    /*
        @Bean(name = "mysqlSqlSessionFactory")
    */
    public SqlSessionFactory sqlSessionFactory(/*@Qualifier("postgreDataSource")*/ PooledDataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/postgresql/*.xml"));

        System.out.println("postgreDataSource sqlSessionFactory  "+dBinfo.toString());
        System.out.println(sessionFactoryBean.getObject().toString());

        return sessionFactoryBean.getObject();
    }

}

/*

public class MysqlConfig {

    public SqlSessionFactory sqlSessionFactory(DBinfo dBinfo) throws Exception {
        String url = "jdbc:mysql://" + dBinfo.getDbUrl() + ':' + dBinfo.getDbPort() + '/' + dBinfo.getDbName();
        //修改地方，添加连接属性
        String conf = "?serverTimezone=GMT%2B8";
        //构建数据库连接池
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url + conf);
        dataSource.setUsername(dBinfo.getUserName());
        dataSource.setPassword(dBinfo.getUserPassword());

        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:dao/mysql/*.xml"));

        return sessionFactoryBean.getObject();
    }

}*/
