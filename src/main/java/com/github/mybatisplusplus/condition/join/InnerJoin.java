package com.github.mybatisplusplus.condition.join;

import com.github.mybatisplusplus.core.IStep;

/**
 * @author niuzhenhao
 * @date 2020/9/15 14:43
 * @desc
 */

public class InnerJoin extends IStep {
    private String tableB;

    private String columnB;

    public InnerJoin(String column, String table, String columnB, String tableB) {
        super(column, table);
        this.columnB = columnB;
        this.tableB = tableB;
    }


    public String getTableB() {
        return tableB;
    }

    public void setTableB(String tableB) {
        this.tableB = tableB;
    }

    public String getColumnB() {
        return columnB;
    }

    public void setColumnB(String columnB) {
        this.columnB = columnB;
    }

    @Override
    public String toSql() {
        return   " inner join " + tableB + " on " + table + "." + column  + " = " + tableB + "." + columnB;
    }
}
