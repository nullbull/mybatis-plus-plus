package com.github.mybatispluspro.core;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.mybatispluspro.condition.In;
import com.github.mybatispluspro.condition.multi.Bewteen;
import com.github.mybatispluspro.condition.single.*;
import com.github.mybatispluspro.condition.Join;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author niuzhenhao
 * @date 2020/9/9 10:25
 * @desc
 */
@Data
public class Query implements ISql {

   private List<ISql> selectList = new ArrayList<>();

   private IStep joinList;

   private List<IStep> havingList = new ArrayList<>();

   private List<IStep> conditionList = new ArrayList<>();

   private AtomicInteger paramNo = new AtomicInteger(0);

   private Object[] param = new Object[20];

   private String sql = "";

   private Class resultType;

   public Query(Class<?> resultType) {
       this.resultType = resultType;
   }

   public Query select(IGet ...select ) {
       List<Select> selectList = Arrays.stream(select).map(a -> {
           String field = getColumn(a);
           String column = camelToUnderline(firstToLowerCase(field));
           String tableName = getTable(a);
           return new Select(column, tableName);
       }).collect(Collectors.toList());
        this.selectList.addAll(selectList);
        return this;
   }

   public Query joinOn(IGet a, IGet b) {
        String field = getColumn(a);
        String fieldB = getColumn(b);
        String columnA = camelToUnderline(firstToLowerCase(field));
        String columnB = camelToUnderline(firstToLowerCase(fieldB));
        String tableName = getTable(a);
        String tableNameB = getTable(b);

        this.joinList = new Join(columnA, tableName, columnB, tableNameB);
        return this;
   }

   public Query eq(IGet a, Object value) {
       String field = getColumn(a);
       String columnA = camelToUnderline(firstToLowerCase(field));
       String tableName = getTable(a);
       this.param[paramNo.get()] = value;
       this.conditionList.add(new Eq(columnA, tableName, value, paramNo.getAndIncrement()));
       return this;
   }

    public Query ge(IGet a, Object value) {
        String field = getColumn(a);
        String columnA = camelToUnderline(firstToLowerCase(field));
        String tableName = getTable(a);
        this.param[paramNo.get()] = value;
        this.conditionList.add(new Ge(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query between(IGet a, Object param1, Object param2) {
        String field = getColumn(a);
        String columnA = camelToUnderline(firstToLowerCase(field));
        String tableName = getTable(a);
        this.param[paramNo.get()] = param1;
        this.param[paramNo.get() + 1] = param2;
        this.conditionList.add(new Bewteen(columnA, tableName, param1, param2, paramNo.getAndIncrement(), paramNo.getAndIncrement()));
        return this;
    }


    public Query gt(IGet a, Object value) {
        String field = getColumn(a);
        String columnA = camelToUnderline(firstToLowerCase(field));
        String tableName = getTable(a);
        this.param[paramNo.get()] = value;
        this.conditionList.add(new Gt(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query lt(IGet a, Object value) {
        String field = getColumn(a);
        String columnA = camelToUnderline(firstToLowerCase(field));
        String tableName = getTable(a);
        this.param[paramNo.get()] = value;
        this.conditionList.add(new Lt(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query le(IGet a, Object value) {
        String field = getColumn(a);
        String columnA = camelToUnderline(firstToLowerCase(field));
        String tableName = getTable(a);
        this.param[paramNo.get()] = value;
        this.conditionList.add(new Ge(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query in(IGet a, Object... value) {
        String field = getColumn(a);
        String columnA = camelToUnderline(firstToLowerCase(field));
        String tableName = getTable(a);
        int length = value == null ? 0 : value.length;
        //如果参数数组大小不够了 扩容
        if (param.length - paramNo.get() < length) {
            Object[] paramNew = new Object[paramNo.get() + length + 20];
            System.arraycopy(param, 0, paramNew, 0, paramNo.get());
            this.param = paramNew;
        }
        for (int i = 0; i < length; i++) {
            param[i + paramNo.get()] = value[i];
        }
        this.conditionList.add(new In(columnA, tableName, paramNo.getAndAdd(value == null ? 0 : value.length), value));
        return this;
    }


    public Query like(IGet a, String value) {
        String field = getColumn(a);
        String columnA = camelToUnderline(firstToLowerCase(field));
        String tableName = getTable(a);
        this.param[paramNo.get()] =  SQLConstant.PERCENT_SIGN + value + SQLConstant.PERCENT_SIGN;
        this.conditionList.add(new Like(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query rightLike(IGet a, String value) {
        String field = getColumn(a);
        String columnA = camelToUnderline(firstToLowerCase(field));
        String tableName = getTable(a);
        this.param[paramNo.get()] = value + SQLConstant.PERCENT_SIGN;
        this.conditionList.add(new RightLike(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }


    /**
     * 首字母转换小写
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String firstToLowerCase(String param) {
        if (StringUtils.isBlank(param)) {
            return EMPTY;
        }
        return param.substring(0, 1).toLowerCase() + param.substring(1);
    }




    /**
     * 字符串驼峰转下划线格式
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String camelToUnderline(String param) {
        if (StringUtils.isBlank(param)) {
            return EMPTY;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }


    public static <T> SerializedLambda getSerializedLambda(Serializable lambda) {
        SerializedLambda serializedLambda = null;
        try {
            Method writeReplace = lambda.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(Boolean.TRUE);
            serializedLambda = (SerializedLambda) writeReplace.invoke(lambda);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return serializedLambda;
    }

    public static <T> String getColumn(IGet<T> lambda) {
        SerializedLambda serializedLambda = getSerializedLambda(lambda);
        String implMethodName = serializedLambda.getImplMethodName();
        System.out.println(implMethodName);
        return implMethodName.substring(3);
    }

    public static <T> String getTable(IGet<T> lambda) {
        SerializedLambda serializedLambda = getSerializedLambda(lambda);
        try {
            Class<?> aClass = Class.forName(serializedLambda.getImplClass().replace("/", "."));
            TableName annotation = aClass.getAnnotation(TableName.class);
            if (null != annotation) {
                String value = annotation.value();
                return value;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return EMPTY;
    }






    @Override
    public String toSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(selectSql());
        sb.append(fromSql());
        sb.append(SQLConstant.WHERE);
        sb.append(conditionSql());
        return sb.toString();

    }

    public Query end() {
        this.sql = toSql();
        return this;
    }

    private String selectSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        String collect = selectList.stream().map(a -> (Select)a).map(Select::toSql).collect(Collectors.joining(","));
        sb.append(collect);
        return sb.toString();
    }

    private String fromSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(SQLConstant.FORM);

        if (null == joinList) {
            sb.append(conditionList.get(0).getColumn());
        } else {
            Join join = (Join)joinList;
            sb.append(join.getTable()).append(join.toSql());
        }
        this.sql = sb.toString();
        return sb.toString();
    }

    private String conditionSql() {
        String condition = conditionList.stream().map(ISql::toSql).collect(Collectors.joining(" and "));
        return condition;
    }

    public static void main(String[] args) {
    }


}
