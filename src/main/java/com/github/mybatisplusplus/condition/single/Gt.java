package com.github.mybatisplusplus.condition.single;

/**
 * @author niuzhenhao
 * @date 2020/9/14 17:22
 * @desc
 */

public class Gt extends SingleCondition {

    public Gt(String column, String table, Object value, int param) {
        super(column, table, value, param);
        this.condition = GT;
    }



}
