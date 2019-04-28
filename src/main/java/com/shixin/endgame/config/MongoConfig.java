package com.shixin.endgame.config;

import com.mongodb.MongoClientURI;
import com.shixin.endgame.entity.DBinfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class MongoConfig {

    //private DBinfo dBinfo;

    /*    public MonogoConfig(DBinfo dBinfo){
            this.dBinfo = dBinfo;
        }*/
    public MongoDbFactory mongoDbFactory(DBinfo dBinfo) {
        String url = "mongodb://" + dBinfo.getDbUrl() + "/" + dBinfo.getDbName(); //+ dBinfo.getUserName() + ":" + dBinfo.getUserPassword()+ "@"
        System.out.println("MongoDB" + url);
        MongoClientURI mongoClientURI = new MongoClientURI(url);
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClientURI);

        return mongoDbFactory;
    }

/*    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory){
        MongoTemplate mongo = new MongoTemplate(mongoDbFactory);
        return mongo;
    }*/
}
