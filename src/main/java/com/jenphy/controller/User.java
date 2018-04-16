package com.jenphy.controller;

import com.jenphy.base.BaseEntity;

import javax.persistence.Id;

/**
 * Created by zhuang on 2018/4/16.
 */
public class User extends BaseEntity {

    @Id
    public String userId;
    public String name;
    public String company;

    public String getUserId() {
        return userId;
    }

    public User setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public User setCompany(String company) {
        this.company = company;
        return this;
    }
}
