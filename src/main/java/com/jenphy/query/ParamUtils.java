package com.jenphy.query;

import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

public class ParamUtils {

    public static final String QUERY_OBJECT = "_queryObject_";
    public static final String ENTITY_CONDITION = "_entityCondition_";
    public static final String IN_CONDITION = "_inCondition_";
    public static final String NOT_IN_CONDITION = "_notInCondition_";

    public static Query setPageInfo(Query query) {

        if (query.pageNo == null) {
            query.pageNo = 1;
        }
        if (query.pageSize == null) {
            query.pageSize = 20;
        }
        return query;
    }

    public static <T> Map<String, Object> getParamMap(T entityCondition, Query query) {
        Map<String, Object> param = new HashMap<>();
        // query对象传递
        param.put(QUERY_OBJECT, query);
        // 实体参数查询条件
        param.put(ENTITY_CONDITION, entityCondition);
        // query中的参数
        if (query != null) {
            // addParam中的参数
            query.param.forEach((paramName, paramValue) -> {
                param.put(paramName, paramValue);
            });
            // inCondition
            Map<String, Object[]> inParamMap = new HashMap<>();
            query.inList.forEach(inCondition -> {
                // #{_inCondition_.t__userId[0]}, #{_inCondition_.t__userId[1]}
                String column = inCondition.column.replaceAll("\\.", "__");
                for (int i=0; i<inCondition.param.size(); i++) {
                    if (CollectionUtils.isNotEmpty(inCondition.param)) {
                        inParamMap.put(column, inCondition.param.toArray());
                    }
                }
                param.put(ParamUtils.IN_CONDITION, inParamMap);
            });
            // notInCondition
            Map<String, Object[]> notInParamMap = new HashMap<>();
            query.inList.forEach(notInCondition -> {
                // #{_notInCondition_.t__userId[0]}, #{_notInCondition_.t__userId[1]}
                String column = notInCondition.column.replaceAll("\\.", "__");
                for (int i=0; i<notInCondition.param.size(); i++) {
                    if (CollectionUtils.isNotEmpty(notInCondition.param)) {
                        notInParamMap.put(column, notInCondition.param.toArray());
                    }
                }
                param.put(ParamUtils.NOT_IN_CONDITION, notInParamMap);
            });
        }
        return param;
    }

}
