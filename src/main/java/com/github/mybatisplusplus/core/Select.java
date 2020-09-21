package com.github.mybatisplusplus.core;

/**
 * @author niuzhenhao
 * @date 2020/9/9 10:25
 * @desc
 */
public class Select extends IStep{


    public Select(String column, String table) {
        super(column, table);
    }

    @Override
    public String toSql() {
        return table + "." + column;
    }
}
