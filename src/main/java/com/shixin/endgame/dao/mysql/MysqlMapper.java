package com.shixin.endgame.dao.mysql;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface MysqlMapper {

    //获取数据库中的表名
    List<Map<String ,Object>> getTableName(@Param("db_name") String db_name);
    //获取表中的数据
    List<Map<String,Object>> getTableData(@Param("table_name") String table_name);
    //获取表中的元数据
    List<Map<String,Object>> getTableMetaData(@Param("db_name") String db_name, @Param("table_name") String table_name);
    //去除重复数据
    void delDuplicatedData(@Param("table_name") String table_name,@Param("columns_name") String columns_name,@Param("pri_name") String pri_name,@Param("table_id") String table_id);
    //删除缺失项
    void delDataByNullAnd(@Param("table_name") String table_name,@Param("columns_name") List<String> columns_name);
    void delDataByNullOr(@Param("table_name") String table_name,@Param("columns_name") List<String> columns_name);
    //删除一列
    void deleteTableColumn(@Param("table_name") String table_name,@Param("columns_name") String columns_name);
    //按条件删除
    void deleteByCondition(@Param("table_name") String table_name, @Param("conditionList") List<String> conditionList);

    //修改字段类型
    void updateColumnType(@Param("table_name") String table_name,@Param("column_name") String column_name,@Param("column_type") String column_type);

    // 获得一列数据和主键
    List<Map<String,Object>> getColumnData(@Param("table_name") String table_name,@Param("column_name") String column_name,@Param("pri_key") String pri_key);
    //修改一列数据
    void setColumnData(@Param("table_name") String table_name,@Param("column_name") String column_name,@Param("column_value") String column_value,@Param("pri_key") String pri_key,@Param("pri_value") String pri_value);


    // 获得平均数
    Double getAverage(@Param("table_name") String table_name,@Param("column_name") String column_name);
    //获得众数，有可能不是一个
    List<Map<String,Object>> getMode(@Param("table_name") String table_name,@Param("column_name") String column_name);
    //获取一列数值
    List<Double> getColumnDouble(@Param("table_name") String table_name,@Param("column_name") String column_name);
    //获得中位数
    Double getMedian(@Param("table_name") String table_name,@Param("column_name") String column_name);
    // 补全字段
    void completFiled(@Param("table_name") String table_name,@Param("column_name") String column_name,@Param("column_value") Double column_value);


    List<Map<String,Object>> getUserDefineData(@Param("table_name") String table_name,@Param("columns_name") List<String> columns_name);

    void insertData(@Param("table_name") String table_name,@Param("columns_name") List<String> columns_name,@Param("column_value") String column_value);

}
