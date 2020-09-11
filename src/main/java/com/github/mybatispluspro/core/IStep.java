package com.github.mybatispluspro.core;

/**
 * @author niuzhenhao
 * @date 2020/9/9 14:01
 * @desc
 */

public abstract class IStep implements ISql, SQLConstant {

    protected String column;

    protected String table;

    public IStep(String column, String table) {
        this.column = column;
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public  abstract String toSql();
}
