package com.shixin.endgame.dao.mysql;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface MysqlMapper {
    List<Map<String,Object>> getData(@Param("table_name") String tableName);
    List<Map<String ,Object>> getTables(@Param("db_name") String db_name);


    List<Map<String,Object>> getTableData(@Param("table_name") String table_name);


    List<String>  selectAllUser() throws Exception ;
    //查询所有数据
    List<Map<String,Object>> getAlldate(@Param("table_name") String tableName);
}
