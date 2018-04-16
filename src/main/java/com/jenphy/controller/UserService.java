package com.jenphy.controller;

import com.jenphy.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhuang on 2018/4/16.
 */
@Service
public class UserService extends BaseService<User> {

    @Autowired
    UserMapper mapper;
}
