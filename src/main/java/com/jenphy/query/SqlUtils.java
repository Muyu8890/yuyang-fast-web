package com.jenphy.query;


import com.jenphy.util.EntityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class SqlUtils {


    public static void main(String [] args) {

        List<String> stringList = new ArrayList<>();

        stringList.add("a, b");
        stringList.add("c");
        String str = StringUtils.join(stringList.toArray(), ", ");
        System.out.println(str);

        System.out.println("t.userId".replaceAll(".", "__"));

    }

    private static LinkedHashSet<String> getSelectColumnSet(Class<?> entityClass, final String aliasName) {
        LinkedHashSet<String> columnSet = new LinkedHashSet<>();
        List<Field> fieldList = EntityUtils.getTableFieldList(entityClass);
        fieldList.forEach(field -> {
            field.setAccessible(true);
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null && StringUtils.isNotEmpty(columnAnnotation.name())) {
                String annotationName = aliasName + "." + columnAnnotation.name();
                columnSet.add(annotationName);
                columnSet.add(annotationName + " as " + field.getName());
            } else {
                columnSet.add(aliasName + "." + field.getName());
            }
        });
        return columnSet;
    }
    public static String getSelectAndFrom(Class entityClass, Query query){
        String columns;
        if (query != null && CollectionUtils.isNotEmpty(query.selectColumnList)){
            columns = StringUtils.join(query.selectColumnList.toArray(), ", ");
        } else {
            LinkedHashSet<String> columnSet = getSelectColumnSet(entityClass, "t");
            StringBuilder columnSb = new StringBuilder();
            int i = 0;
            for (String column : columnSet) {
                i++;
                columnSb.append(column);
                if (i < columnSet.size()) {
                    columnSb.append(", ");
                }
            }
            columns = columnSb.toString();
        }
        return "select " + columns + " from " + EntityUtils.getTableName(entityClass) + " t ";
    }

    public static String getSelectCountAndFrom(Class entityClass){
        // TODO count(*),count(1),count(id)
        return "select count(1) from " + EntityUtils.getTableName(entityClass) + " t ";
    }


    public static String getJoinAndOnCondition(Query query){
        if (query == null) {
            return "";
        }
        StringBuffer resultBuffer = new StringBuffer();
        query.joinList.forEach(join -> {
            resultBuffer.append(join.joinType.getValue() + join.tableNameAndOnConditions + " ");
        });
        return resultBuffer.toString();
    }



    public static String getWhere(Object entityCondition, Query query){
        StringBuffer resultBuffer = new StringBuffer(" where 1=1 ");
        if (entityCondition != null) {
            String where = "";
            List<Field> fieldList = EntityUtils.getTableFieldList(entityCondition.getClass());
            for (Field field : fieldList) {
                Object value = EntityUtils.getValue(entityCondition, field);
                field.setAccessible(true);
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName;
                if (columnAnnotation != null && StringUtils.isNotEmpty(columnAnnotation.name())) {
                    columnName = columnAnnotation.name();
                } else {
                    columnName = field.getName();
                }
                if (value != null) {
                    where += " and t." + columnName + " = #{" + ParamUtils.ENTITY_CONDITION + "." + field.getName() + "}";
                }
            }
            resultBuffer.append(where);
        }
        if (query != null) {
            // query.whereList
            if (CollectionUtils.isNotEmpty(query.whereList)) {
                String where = StringUtils.join(query.whereList.toArray(), ") and (");
                resultBuffer.append(" and (").append(where).append(")");
            }
            // query.inCondition
            query.inList.forEach(inCondition -> {
                if (CollectionUtils.isEmpty(inCondition.param)) {
                    resultBuffer.append(" and (1=0) ");
                } else {
                    // #{_inCondition_.t__userId[0]}, #{_inCondition_.t__userId[1]}
                    String column = inCondition.column.replaceAll("\\.", "__");
                    List<String> inParamList = new ArrayList<>();
                    for (int i=0; i<inCondition.param.size(); i++) {
                        inParamList.add("#{" + ParamUtils.IN_CONDITION + "." + column + "[" + i + "]}");
                    }
                    String inSql = " and " + inCondition.column + " in (" + StringUtils.join(inParamList.toArray(), ", ") + ") ";
                    resultBuffer.append(inSql);
                }
            });

            // query.notInCondition
            query.notInList.forEach(notInCondition -> {
                if (CollectionUtils.isNotEmpty(notInCondition.param)) {
                    // #{_notInCondition_.t__userId[0]}, #{_notInCondition_.t__userId[1]}
                    String column = notInCondition.column.replaceAll("\\.", "__");
                    List<String> notInParamList = new ArrayList<>();
                    for (int i=0; i<notInCondition.param.size(); i++) {
                        notInParamList.add("#{" + ParamUtils.NOT_IN_CONDITION + "." + column + "[" + i + "]}");
                    }
                    String inSql = " and " + notInCondition.column + " not in (" + StringUtils.join(notInParamList.toArray(), ", ") + ") ";
                    resultBuffer.append(inSql);
                }
            });

        }
        return resultBuffer.toString();
    }

    public static String getOrderBy(Query query){
        if (query != null && CollectionUtils.isNotEmpty(query.orderByList)) {
            return " ORDER BY " + StringUtils.join(query.orderByList.toArray(), ", ") + " ";
        } else {
            return "";
        }
    }

    public static String getGroupBy(Query query){
        if (query != null && StringUtils.isNotBlank(query.groupBy)) {
            return " GROUP BY " + query.groupBy + " ";
        } else {
            return "";
        }
    }

    public static String getLimit(Query query){
        return " limit "+ (query.pageNo - 1) * query.pageSize +", " + query.pageSize + " ";
    }



}
