package com.jenphy.query;

import java.util.*;

public class Query {

    protected Integer pageNo;
    protected Integer pageSize;

    final protected List<String> selectColumnList = new ArrayList<>();
    final protected List<String> whereList = new ArrayList<>();
    final public List<InCondition> inList = new ArrayList<>(); // TODO
    final protected List<InCondition> notInList = new ArrayList<>(); // TODO
    final protected List<String> orderByList = new ArrayList<>();
    protected String groupBy = "";
    final protected List<Join> joinList = new ArrayList<>();
    final protected Map<String, Object> param = new HashMap<>();

    public Query addParam(String paramName, Object paramValue){
        param.put(paramName, paramValue);
        return this;
    }

    public Query addSelectColumn(String selectColumn){
        selectColumnList.add(selectColumn);
        return this;
    }


    public Query addWhere(String where){
        whereList.add(where);
        return this;
    }

    public Query addIn(String column, Collection param){
        inList.add(new InCondition(column, param));
        return this;
    }

    public Query addIn(String column, Object... params){
        inList.add(new InCondition(column, Arrays.asList(params)));
        return this;
    }

    public Query addNotIn(String column, Collection param){
        notInList.add(new InCondition(column, param));
        return this;
    }

    public Query addNotIn(String column, Object... params){
        notInList.add(new InCondition(column, Arrays.asList(params)));
        return this;
    }

    public Query addOrderBy(String columns){
        orderByList.add(columns);
        return this;
    }

    public Query addGroupBy(String columns){
        groupBy = columns;
        return this;
    }


    public Query addJoin(JoinType joinType,
                         String tableNameAndOnConditions){
        joinList.add(new Join(joinType, tableNameAndOnConditions));
        return this;
    }

    ///////////////////////////////
    public Query setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Query setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    ///////////////////////////////
    /**
     * 连接类型
     */
    public enum JoinType{
        JOIN(" JOIN "),
        LEFT_JOIN(" LEFT JOIN "),
        RIGHT_JOIN(" RIGHT JOIN "),
        ;
        String value;
        JoinType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * in查询条件
     */
    public class InCondition{
        protected String column;
        public Collection param;

        public InCondition(String column, Collection param) {
            this.column = column;
            this.param = param;
        }
    }

    protected class Join {
        protected JoinType joinType;
        protected String tableNameAndOnConditions;

        private Join(JoinType joinType, String tableNameAndOnConditions) {
            this.joinType = joinType;
            this.tableNameAndOnConditions = tableNameAndOnConditions;
        }
    }

}
