package com.jenphy.base;

import com.jenphy.query.ParamUtils;
import com.jenphy.query.Query;
import com.jenphy.query.SqlUtils;
import com.jenphy.util.EntityUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class BaseMapperProvider {

    public String get(Map paramMap) {
        Class entityClass = (Class) paramMap.get("entityClass");
        Field idField = EntityUtils.getIdField(entityClass);

        Query query = new Query();
        query.addWhere("t." + idField.getName() + " = #{id}");
        String mybatisSql = "";
        mybatisSql += SqlUtils.getSelectAndFrom(entityClass, null);
        mybatisSql += SqlUtils.getJoinAndOnCondition(query);
        mybatisSql += SqlUtils.getWhere(null, query);
        return mybatisSql;
    }

    public String find(Map paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        String mybatisSql = "";
        mybatisSql += SqlUtils.getSelectAndFrom(entityCondition.getClass(), query);
        mybatisSql += SqlUtils.getJoinAndOnCondition(query);
        mybatisSql += SqlUtils.getWhere(entityCondition, query);
        return mybatisSql;
    }

    public String findList(Map paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);

        String mybatisSql = "";
        mybatisSql += find(paramMap);
        mybatisSql += SqlUtils.getGroupBy(query);
        mybatisSql += SqlUtils.getOrderBy(query);
        return mybatisSql;

    }

    public String logicDelete(Map paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        StringBuffer sql = new StringBuffer();
        // update
        sql.append("update ").append(EntityUtils.getTableName(entityCondition.getClass())).append(" t ");
        // join
        sql.append(SqlUtils.getJoinAndOnCondition(query));
        // set
        sql.append(" set t.deleteFlag = true ");
        // where
        sql.append(SqlUtils.getWhere(entityCondition, query));
        return sql.toString();
    }

    public String count(Map paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        String mybatisSql = "";
        mybatisSql += SqlUtils.getSelectCountAndFrom(entityCondition.getClass());
        mybatisSql += SqlUtils.getJoinAndOnCondition(query);
        mybatisSql += SqlUtils.getWhere(entityCondition, query);
        mybatisSql += SqlUtils.getGroupBy(query);
        return mybatisSql;
    }

    public String findPageList(Map paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        String mybatisSql = "";
        mybatisSql += findList(paramMap);
        mybatisSql += SqlUtils.getLimit(query);
        return mybatisSql;
    }

    public <T> String saveBatch(Map paramMap) {
        Collection<T> entityCollection = (Collection<T>) paramMap.get("entityCollection");
        String tableName = null;
        Set<String> fieldNameSet = new HashSet<>();
        List<String> fieldNameList = new ArrayList<>();
        for (T entity : entityCollection) {
            if (StringUtils.isBlank(tableName)) {
                tableName = EntityUtils.getTableName(entity.getClass());
            }
            for (Field field : EntityUtils.getTableFieldList(entity.getClass())) {
                if (!fieldNameSet.contains(field.getName())) {
                    if (EntityUtils.getValue(entity, field) != null) {
                        fieldNameSet.add(field.getName());
                        fieldNameList.add(field.getName());
                    }
                }
            }
        }
        StringBuffer valueParams = new StringBuffer();
        for (int i = 0; i < entityCollection.size(); i++) {
            valueParams.append("(");
            for (int j = 0; j < fieldNameList.size(); j++) {
                String fieldName = fieldNameList.get(j);
                valueParams.append("#{").append(fieldName).append("__").append(i).append("__}");
                if (j != fieldNameList.size() - 1) {
                    valueParams.append(", ");
                }
            }
            valueParams.append(")");
            if (i != entityCollection.size() - 1) {
                valueParams.append(", ");
            }
        }
        StringBuffer sql = new StringBuffer();
        // insert into
        sql.append("insert into ").append(tableName).append(" ");
        // fields
        sql.append(" (").append(StringUtils.join(fieldNameList.toArray(), ", ")).append(") ");
        // values
        sql.append(" values ").append(valueParams);
        return sql.toString();
    }

    private <T> String update(T entity, boolean isUpdateNumField) {
        Field idField = null;
        String sql = "";
        Annotation tableAnnotation = entity.getClass().getAnnotation(Table.class);
        String tableName = ((Table) tableAnnotation).name();
        List<String> fieldParamList = new ArrayList<>();
        for (Field field : EntityUtils.getTableFieldList(entity.getClass())) {
            field.setAccessible(true);
            //处理主键
            if (null != field.getAnnotation(Id.class) || null != field.getAnnotation(Transient.class)) {
                idField = field;
                continue;
            }
            if (!isUpdateNumField) {
                if (EntityUtils.getValue(entity, field.getName()) == null) {
                    continue;
                }
            }
            fieldParamList.add(field.getName() + " = #{" + field.getName() + "}");
        }
        String update = "update " + tableName;
        String set = " set " + StringUtils.join(fieldParamList.toArray(), ", ");
        String where = " where " + idField.getName() + " = #{" + idField.getName() + "}";
        sql += update;
        sql += set;
        sql += where;
        return sql;

    }

    public <T> String update(T entity) {
        return update(entity, true);
    }

    public <T> String updateNotNull(T entity) {
        return update(entity, false);
    }

}
