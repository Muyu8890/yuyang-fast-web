package com.jenphy.base;

import com.fasterxml.jackson.annotation.*;
import com.jenphy.constants.Constants;
import com.jenphy.util.DateUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础实体类
 * Created by zhuang on 2018/4/12.
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -4287607489867805101L;

    //
    // 下面是标准 WHO 字段
    // ----------------------------------------------------------------------------------------------------
    /**
     * 创建人ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String creatorId;
    /**
     * 创建人名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String creatorName;
    /**
     * 创建时间
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = DateUtils.DEFAULT_PATTERN)
    private Date createDate;

    /**
     * 更新人ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updaterId;
    /**
     * 更新人名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String updaterName;
    /**
     * 更新时间
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = DateUtils.DEFAULT_PATTERN)
    private Date updateDate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String toJSONString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public String getCreatorId() {
        return creatorId;
    }

    public BaseEntity setCreatorId(String creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public BaseEntity setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public BaseEntity setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public BaseEntity setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
        return this;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public BaseEntity setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
        return this;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public BaseEntity setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
        return this;
    }

}
