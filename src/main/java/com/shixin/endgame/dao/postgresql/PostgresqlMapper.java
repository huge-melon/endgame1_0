package com.shixin.endgame.dao.postgresql;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PostgresqlMapper {

    //获取数据库中的表名
    List<Map<String ,Object>> getTableName(@Param("db_name") String db_name);
    //获取表中的数据
    List<Map<String,Object>> getTableData(@Param("table_name") String table_name);
    //获取表中的元数据
    List<Map<String,Object>> getTableMetaData(@Param("table_name") String table_name);
    List<Map<String,Object>> getPriKey(@Param("table_name") String table_name);

    //去除重复数据
    void delDuplicatedData(@Param("table_name") String table_name,@Param("columns_name") String columns_name,@Param("pri_name") String pri_name,@Param("table_id") String table_id);

    //删除缺失项
    void delDataByNullAnd(@Param("table_name") String table_name,@Param("columns_name") List<String> columns_name);
    void delDataByNullOr(@Param("table_name") String table_name,@Param("columns_name") List<String> columns_name);
}
