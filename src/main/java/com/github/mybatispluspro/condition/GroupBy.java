package com.github.mybatispluspro.condition;

import com.github.mybatispluspro.core.IStep;

/**
 * @author niuzhenhao
 * @date 2020/9/14 17:37
 * @desc
 */

public class GroupBy extends IStep {

    public GroupBy(String column, String table) {
        super(column, table);
    }

    @Override
    public String toSql() {
        return GROUP_BY + table + "." + column;
    }
}
