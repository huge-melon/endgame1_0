package com.shixin.endgame.config;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.shixin.endgame.mapper.mysql",sqlSessionFactoryRef = "mysqlSqlSessionFactory")

public class MysqlConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Bean(name="mysqlDataSource")
    @ConfigurationProperties(prefix = "datasource.mysql")

    public PooledDataSource mysqlDataSource(){
        logger.info("create mysql datasource");
        PooledDataSource d=null;
        try{
            Properties p = PropertiesLoaderUtils.loadProperties(new ClassPathResource("MyDataSource.properties"));
            String dbDriver = p.getProperty("datasource.mysql.driver-class-name");
            String connString = p.getProperty("datasource.mysql.url");
            String dbUser = p.getProperty("datasource.mysql.username");
            String dbPassword = p.getProperty("datasource.mysql.password");
            d = new PooledDataSource(dbDriver,connString,dbUser,dbPassword);
            logger.info("Mysql datasource:" + d);
        }
        catch(IOException e) {
            logger.error("Mysql 数据源配置失败！");
        }
        finally {
            return d;
        }
    }

    @Bean(name = "mysqlSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
        logger.info("create mysql sqlSessionFactory");
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/mysql/*.xml"));
        return sessionFactoryBean.getObject();
    }
}
