package com.jenphy.constants;

import com.jenphy.base.BaseEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础枚举值
 * Created by zhuang on 2018/4/12.
 */
public enum BaseEnums implements BaseEnum {
    SUCCESS("request.success", "请求成功"),

    FAILURE("request.failure", "请求失败"),

    OPERATION_SUCCESS("operation.success", "操作成功"),

    OPERATION_FAILURE("operation.failure", "操作失败"),

    ERROR("system.error", "系统异常"),

    NOT_FOUND("not_found", "请求资源不存在"),

    FORBIDDEN("forbidden", "无权限访问"),

    VERSION_NOT_MATCH("record_not_exists_or_version_not_match", "记录版本不存在或不匹配"),

    PARAMETER_NOT_NULL("parameter_not_be_null", "参数不能为空"),

    SET_COLLECTION_ERROR("set_collection_error", "设置集合异常"),

    GET_PROPERTY_ERROR("get_property_error", "获取对象属性异常"),

    ZERO_NOT_IN_COLLECTION("zero_not_in_collection", "{0}不是集合属性"),

    SET_ID_ERROR("set_id_error", "设置id值异常"),

    MAPPER_NOT_FOUND("mapper_not_found", "service中没有覆盖getMapper方法也没有mapper字段");

    private String code;

    private String desc;

    private static Map<String, String> allMap = new HashMap<>();

    BaseEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    static {
        for(BaseEnums enums : BaseEnums.values()){
            allMap.put(enums.code, enums.desc);
        }
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String desc() {
        return desc;
    }

    public String desc(String code) {
        return allMap.get(code);
    }
}
