package com.shixin.endgame.common;

public enum QueryType {
    RANGE("RANG"),LIKE("LIKE"),NORMAL("NORMAL");
    private String type;
    // 构造方法
    private QueryType(String type) {
        this.type = type;
    }
}