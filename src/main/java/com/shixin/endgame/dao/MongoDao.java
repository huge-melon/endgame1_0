package com.shixin.endgame.dao;

import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class MongoDao {

    private MongoTemplate mongoTemplate;

    public MongoDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    //获取集合中的数据
    public List<Map> findMongoData(String collectName){
        System.out.println("MongoDBDao:");
        System.out.println(mongoTemplate.findAll(Map.class));
        return mongoTemplate.findAll(Map.class,collectName);
    }
    //获取所有集合名
    public Set<String> getTableName(){
        return mongoTemplate.getCollectionNames();
    }


}
