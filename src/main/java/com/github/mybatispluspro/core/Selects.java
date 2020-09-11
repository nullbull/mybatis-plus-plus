package com.github.mybatispluspro.core;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author niuzhenhao
 * @date 2020/9/9 15:13
 * @desc
 */
@Data
public class Selects implements ISql {

    private List<Select> selectList;

    private String mainTable;

    public Selects(List<Select> selectList) {
        this.selectList = selectList;
        this.mainTable = selectList.get(0).getTable();
    }

    @Override
    public String toSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        String collect = selectList.stream().map(Select::toSql).collect(Collectors.joining(","));
        sb.append(collect);
        sb.append(" ");
        sb.append(" from ")
                .append(mainTable);
        return sb.toString();
    }
}
