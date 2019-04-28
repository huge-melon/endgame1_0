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
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import java.io.IOException;
import java.util.Properties;


@Configuration
//@MapperScan(basePackages = "com.shixin.endgame.dao.mysql",sqlSessionFactoryRef = "mysqlSqlSessionFactory")
@MapperScan(basePackages = "com.shixin.endgame.dao.mysql")

public class MysqlConfig {

    /*  private DBinfo dBinfo;
      //private final Logger logger = LoggerFactory.getLogger(this.getClass());
  */
    public MysqlConfig() {

    }
   /* public MysqlConfig(DBinfo db){
        dBinfo=db;
        System.out.println("Const:  "+dBinfo.toString());

    }*/


//    @Bean(name = "mysqlDataSource")

    public SqlSessionFactory mysqlSqlSessionFactory(DBinfo dBinfo) throws Exception {

        System.out.println("dataSource:  " + dBinfo.toString());
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
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/mysql/*.xml"));

        System.out.println("sqlSessionFactory" + dBinfo.toString());
        System.out.println(sessionFactoryBean.getObject().toString());

        return sessionFactoryBean.getObject();

    }


/*
    @Bean(name = "mysqlSqlSessionFactory")
*/
    /*   public SqlSessionFactory sqlSessionFactory(*//*@Qualifier("mysqlDataSource")*//* PooledDataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/mysql/*.xml"));

        System.out.println("sqlSessionFactory"+dBinfo.toString());
        System.out.println(sessionFactoryBean.getObject().toString());

        return sessionFactoryBean.getObject();
    }
*/
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
