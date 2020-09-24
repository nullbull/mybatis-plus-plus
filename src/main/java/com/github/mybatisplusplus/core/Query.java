package com.github.mybatisplusplus.core;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.mybatisplusplus.annotation.ColumnTable;
import com.github.mybatisplusplus.condition.GroupBy;
import com.github.mybatisplusplus.condition.In;
import com.github.mybatisplusplus.condition.OrderBy;
import com.github.mybatisplusplus.condition.join.LeftJoin;
import com.github.mybatisplusplus.condition.join.RightJoin;
import com.github.mybatisplusplus.condition.multi.Bewteen;
import com.github.mybatisplusplus.condition.single.*;
import com.github.mybatisplusplus.condition.join.Join;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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

   private List<ISql> generateSelectList = new ArrayList<>();

   private IStep join;

   private List<IStep> havingList = new ArrayList<>();

   private List<IStep> conditionList = new ArrayList<>();

   private List<IStep> endList = new ArrayList<>();

   private AtomicInteger paramNo = new AtomicInteger(0);

   private Object[] param = new Object[20];

   private String sql = "";

   private Class resultType;

   private String baseTable;


   private static ConcurrentHashMap<String, String> TABLE_NAME = new ConcurrentHashMap<>(64);

   public Query(Class<?> resultType) {
       this.resultType = resultType;
       prepareGenerateSelectList();
   }

   //根据查询结果类，构建查询条件
   private void prepareGenerateSelectList() {
       Field[] fields = this.resultType.getDeclaredFields();
       String tableName = "";
       TableName tableNameAnno = (TableName) this.resultType.getAnnotation(TableName.class);
       if (null != tableNameAnno) {
           tableName = tableNameAnno.value();
           this.baseTable = tableName;
       }

       for (Field field : fields) {
           String filedTableName = "";
           ColumnTable tableNameAnnoTemp = field.getAnnotation(ColumnTable.class);
           if (null != tableNameAnnoTemp) {
               filedTableName = tableNameAnnoTemp.table();
           } else {
               filedTableName = tableName;
           }
           this.generateSelectList.add(new Select(camelToUnderline(field.getName()), filedTableName));
       }
   }

   public Query select(IGet ...select ) {
       List<Select> selectList = Arrays.stream(select).map(a -> {
           SerializedLambda serializedLambda = getSerializedLambda(a);
           String field = getColumn(serializedLambda);
           String tableName = getTable(serializedLambda);
           String columnA = camelToUnderline(firstToLowerCase(field));
           return new Select(columnA, tableName);
       }).collect(Collectors.toList());
        this.selectList.addAll(selectList);
        return this;
   }

    public Query joinOn(IGet a, IGet b) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        SerializedLambda serializedLambdaB = getSerializedLambda(b);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));

        String fieldB = getColumn(serializedLambdaB);
        String columnB = camelToUnderline(firstToLowerCase(fieldB));
        String tableNameB = getTable(serializedLambdaB);

        this.join = new Join(columnA, tableName, columnB, tableNameB);
        return this;
    }

   public Query leftJoin(IGet a, IGet b) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        SerializedLambda serializedLambdaB = getSerializedLambda(b);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));

        String fieldB = getColumn(serializedLambdaB);
        String columnB = camelToUnderline(firstToLowerCase(fieldB));
        String tableNameB = getTable(serializedLambdaB);

        this.join = new LeftJoin(columnA, tableName, columnB, tableNameB);
        return this;
   }
    public Query rightJoin(IGet a, IGet b) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        SerializedLambda serializedLambdaB = getSerializedLambda(b);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));

        String fieldB = getColumn(serializedLambdaB);
        String columnB = camelToUnderline(firstToLowerCase(fieldB));
        String tableNameB = getTable(serializedLambdaB);

        this.join = new RightJoin(columnA, tableName, columnB, tableNameB);
        return this;
    }

   public Query eq(IGet a, Object value) {
       SerializedLambda serializedLambda = getSerializedLambda(a);
       String field = getColumn(serializedLambda);
       String tableName = getTable(serializedLambda);
       String columnA = camelToUnderline(firstToLowerCase(field));
       this.param[paramNo.get()] = value;
       this.conditionList.add(new Eq(columnA, tableName, value, paramNo.getAndIncrement()));
       return this;
   }

    public Query ge(IGet a, Object value) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));
        this.param[paramNo.get()] = value;
        this.conditionList.add(new Ge(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query between(IGet a, Object param1, Object param2) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));
        this.param[paramNo.get()] = param1;
        this.param[paramNo.get() + 1] = param2;
        this.conditionList.add(new Bewteen(columnA, tableName, param1, param2, paramNo.getAndIncrement(), paramNo.getAndIncrement()));
        return this;
    }


    public Query gt(IGet a, Object value) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));
        this.param[paramNo.get()] = value;
        this.conditionList.add(new Gt(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query lt(IGet a, Object value) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));
        this.param[paramNo.get()] = value;
        this.conditionList.add(new Lt(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query le(IGet a, Object value) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));
        this.param[paramNo.get()] = value;
        this.conditionList.add(new Ge(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query in(IGet a, Object... value) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));
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
        SerializedLambda serializedLambda = getSerializedLambda(a);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));
        this.param[paramNo.get()] =  SQLConstant.PERCENT_SIGN + value + SQLConstant.PERCENT_SIGN;
        this.conditionList.add(new Like(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query rightLike(IGet a, String value) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));
        this.param[paramNo.get()] = value + SQLConstant.PERCENT_SIGN;
        this.conditionList.add(new RightLike(columnA, tableName, value, paramNo.getAndIncrement()));
        return this;
    }

    public Query groupBy(IGet ...a) {
        if (a != null && a.length > 0 ) {
             SerializedLambda serializedLambda = getSerializedLambda(a[0]);
             String field = getColumn(serializedLambda);
             String tableName = getTable(serializedLambda);
             String columnA = camelToUnderline(firstToLowerCase(field));
             String more = "";
            if (a.length > 1) {
                for (int i = 1; i < a.length; ++i) {
                    SerializedLambda serializedLambdaB = getSerializedLambda(a[i]);
                    String fieldB = getColumn(serializedLambdaB);
                    String tableNameB = getTable(serializedLambdaB);
                    String columnB = camelToUnderline(firstToLowerCase(fieldB));
                    more +=  ","  + tableNameB + "." + columnB;
                }
            }
            this.endList.add(new GroupBy(columnA, tableName, more));
        }

        return this;
   }


    public Query orderByAes(IGet a) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));
        this.endList.add(new OrderBy(columnA, tableName, SQLConstant.AES));
        return this;
    }

    public Query orderByDesc(IGet a) {
        SerializedLambda serializedLambda = getSerializedLambda(a);
        String field = getColumn(serializedLambda);
        String tableName = getTable(serializedLambda);
        String columnA = camelToUnderline(firstToLowerCase(field));
        this.endList.add(new OrderBy(columnA, tableName, SQLConstant.DESC));
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


    public static SerializedLambda getSerializedLambda(Serializable lambda) {
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

    public static String getColumn(IGet lambda) {
        SerializedLambda serializedLambda = getSerializedLambda(lambda);
        String implMethodName = serializedLambda.getImplMethodName();
        return implMethodName.substring(3);
    }

    public static String getTable(IGet lambda) {
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


    public static String getColumn(SerializedLambda serializedLambda) {
        String implMethodName = serializedLambda.getImplMethodName();
        return implMethodName.substring(3);
    }

    public static String getTable(SerializedLambda serializedLambda) {
        String tableName = EMPTY;
        String className = serializedLambda.getImplClass();
        tableName = TABLE_NAME.get(className);
        if (null != tableName) {
            return tableName;
        }
        try {
            Class<?> aClass = Class.forName(className.replace("/", "."));
            TableName annotation = aClass.getAnnotation(TableName.class);
            if (null != annotation) {
                String value = annotation.value();
                TABLE_NAME.put(className, value);
                return value;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return EMPTY;
    }



    public Query end() {
        this.sql = toSql();
        return this;
    }


    @Override
    public String toSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(selectSql());
        sb.append(fromSql());
        sb.append(SQLConstant.WHERE);
        sb.append(conditionSql());
        sb.append(endSql());
        return sb.toString();

    }


    private String selectSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        String collect = "";
        if (!selectList.isEmpty()) {
            collect = selectList.stream().map(a -> (Select)a).map(Select::toSql).collect(Collectors.joining(","));
        } else {
            collect = generateSelectList.stream().map(a -> (Select) a).map(Select::toSql).collect(Collectors.joining(","));
        }
        sb.append(collect);
        return sb.toString();
    }

    private String fromSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(SQLConstant.FORM);

        if (null == join) {
            sb.append(this.baseTable);
        } else {
            sb.append(join.getTable()).append(join.toSql());
        }
        this.sql = sb.toString();
        return sb.toString();
    }

    private String conditionSql() {
        String condition = conditionList.stream().map(ISql::toSql).collect(Collectors.joining(" and "));
        return condition;
    }

    private String endSql() {
        String endSql = endList.stream().map(IStep::toSql).collect(Collectors.joining(" "));
        return endSql;
    }

    public static void main(String[] args) {
    }


}
