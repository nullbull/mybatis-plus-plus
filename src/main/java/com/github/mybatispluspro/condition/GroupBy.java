package com.github.mybatispluspro.condition;

import com.github.mybatispluspro.core.IStep;

/**
 * @author niuzhenhao
 * @date 2020/9/14 17:37
 * @desc
 */

public class GroupBy extends IStep {

    private String more;
    public GroupBy(String column, String table, String more) {
        super(column, table);
        this.more = more;
    }

    @Override
    public String toSql() {
        return GROUP_BY + table + "." + column + more;
    }
}
