package com.jenphy.base;

import com.jenphy.constants.BaseEnums;
import com.jenphy.exception.ErrorException;
import com.jenphy.query.Page;
import com.jenphy.query.ParamUtils;
import com.jenphy.query.Query;
import com.jenphy.util.EntityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public abstract class BaseService<T> {
	private static Logger logger = Logger.getLogger(BaseService.class);

    private BaseMapper mapper;
    // 如果子类中没有getMapper方法会调用baseService中的getMapper方法，在这个方法中直接获取mapper属性
    public  <M extends BaseMapper<T>>M getMapper(){
        if (mapper == null) {
            Field mapperField = EntityUtils.getField(this.getClass(), "mapper");
            mapper = (M)EntityUtils.getValue(this, mapperField);
        }
        // 没有覆盖getMapper方法，也没有mapper字段，抛出异常
        if (mapper == null) {
            throw new ErrorException(BaseEnums.MAPPER_NOT_FOUND);
        }
        return (M)mapper;
    }

	public int save(T entity) {
        List<T> entityList = new ArrayList<>();
        entityList.add(entity);
        return saveBatch(entityList, false);
    }

	public int saveBatch(Collection<T> entityCollection) {
        return saveBatch(entityCollection, true);
	}

    private int saveBatch(Collection<T> entityCollection, boolean batch) {
        if (CollectionUtils.isEmpty(entityCollection)) {
            return 0;
        }
        Date now = new Date();
        Field idField = null;
        List<Field> fieldList = null;
        int i = 0;
        String generateIdValue = now.getTime() + "_" + UUID.randomUUID().toString().replace("-", "");
        for (T entity : entityCollection) {
            fieldList = EntityUtils.getTableFieldList(entity.getClass());

            if (idField == null) {
                idField = EntityUtils.getIdField(entity.getClass());
            }
            // id
            Object idValue = EntityUtils.getValue(entity, idField);
            if (idField != null && idValue == null) {
                if (batch) {
                    idValue = generateIdValue + "_" + i;
                } else {
                    idValue = generateIdValue;
                }
                try {
                    idField.set(entity, idValue);
                } catch (IllegalAccessException e) {
                    throw new ErrorException(BaseEnums.SET_ID_ERROR);
                }
                i++;
            }
        }
        i = 0;
        Map<String, Object> param = new HashMap<>();
        for (T entity : entityCollection) {
            for (Field field : fieldList) {
                param.put(field.getName() + "__" + i + "__", EntityUtils.getValue(entity, field));
            }
            i++;
        }
        param.put("entityCollection", entityCollection);
        // 设置id，创建人，修改人等信息
        return getMapper().saveBatch(param);
    }

    public int saveOrUpdate(T entity){
        Object idValue = EntityUtils.getIdValue(entity);
        if (idValue == null) {
            save(entity);
            return 1;
        } else {
            return update(entity);
        }
    }

    public int saveOrUpdateNotNull(T entity){
        Object idValue = EntityUtils.getIdValue(entity);
        if (idValue == null) {
            save(entity);
            return 1;
        } else {
            return updateNotNull(entity);
        }
    }
    public int update(T entity) {
		return getMapper().update(entity);
	}
    public int updateNotNull(T entity) {
		return getMapper().updateNotNull(entity);
	}
    public void delete(T entity) {
        // TODO
    }
    public void delete(Class<T> entityClass, Serializable id) {
        // TODO
    }
    public void logicDelete(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return;
        }
        getMapper().logicDelete(ParamUtils.getParamMap(entityCondition, query));
    }
    public void logicDelete(T entityCondition) {
        logicDelete(entityCondition, null);
    }
    public T get(Class<T> entityClass, Serializable id) {
	    if (id == null) {
	        return null;
        }
        return getMapper().get(entityClass, id);
    }
    public T find(T entityCondition) {
        return find(entityCondition, null);
    }
    public T find(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return null;
        }
        return getMapper().find(ParamUtils.getParamMap(entityCondition, query));
    }
    public List<T> findList(T entityCondition) {
        return findList(entityCondition, null);
    }
    public List<T> findList(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return new ArrayList<>();
        }
        return getMapper().findList(ParamUtils.getParamMap(entityCondition, query));
    }
    public int count(T entityCondition) {
        return count(entityCondition, null);
    }
    public int count(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return 0;
        }
        return getMapper().count(ParamUtils.getParamMap(entityCondition, query));
    }
    public Page<T> findPage(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return new Page<>(query.getPageNo(), query.getPageSize(), 0, new ArrayList<>());
        }
	    // 设置页数页码
        ParamUtils.setPageInfo(query);
        Map<String, Object> paramMap = ParamUtils.getParamMap(entityCondition, query);
        Page<T> page;
        int count = getMapper().count(paramMap);
        if (count == 0) {
            page = new Page<>(query.getPageNo(), query.getPageSize(), count, new ArrayList<>());
        } else {
            List<T> entityList = getMapper().findPageList(paramMap);
            page = new Page<>(query.getPageNo(), query.getPageSize(), count, entityList);
        }
		return page;
	}


	private boolean isInEmpty(Query query){
        if(query == null) {
            return false;
        }
        for (Query.InCondition inCondition : query.inList) {
            if (CollectionUtils.isEmpty(inCondition.param)) {
                logger.info("query中存在in为空，不进行数据库操作！");
                return true;
            }
        }
        return false;
    }
}
