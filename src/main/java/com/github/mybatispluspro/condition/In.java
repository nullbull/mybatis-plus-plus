package com.github.mybatispluspro.condition;

import com.github.mybatispluspro.core.IStep;

/**
 * @author niuzhenhao
 * @date 2020/9/14 17:42
 * @desc
 */


public class In extends IStep {
    public Object[] param;

    private int pos;
    public In(String column, String table, int pos,  Object ... param) {
        super(column, table);
        this.param = param;
        this.pos = pos;
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder(table + "." + column + IN);
        sql.append(LEFT_BRACKET);
        if (param != null ) {
            for (int i = 0; i < param.length; i++) {
                sql.append("#{query.param[").append(pos++).append("]}");
                if (i < param.length - 1) {
                    sql.append(",");
                }
            }
        }
        sql.append(RIGHT_BRACKET);
        return sql.toString();
    }
}
