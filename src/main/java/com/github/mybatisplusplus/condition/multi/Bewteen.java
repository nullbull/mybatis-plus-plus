package com.github.mybatisplusplus.condition.multi;

import com.github.mybatisplusplus.core.IStep;

/**
 * @author niuzhenhao
 * @date 2020/9/14 17:02
 * @desc
 */

public class Bewteen extends IStep {
    private Object param1;

    private Object param2;

    private int paramPos1;

    private int paramPos2;

    public Bewteen(String column, String table, Object param1, Object param2, int paramPos1, int paramPos2) {
        super(column, table);
        this.param1 = param1;
        this.param2 = param2;
        this.paramPos1 = paramPos1;
        this.paramPos2 = paramPos2;
    }

    @Override
    public String toSql() {
        return table + "." + column + BETWEEN + "#{param" +  paramPos1 + "}"
                +  AND  + "#{param" + paramPos2 + "}";
    }
}
