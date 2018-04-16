package com.jenphy.controller;

import com.jenphy.base.Result;
import com.jenphy.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhuang on 2018/4/12.
 */
@RestController
public class demoController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public Result hello(User user) {

        List<User> list = userService.findList(user);
        return ResultUtils.successWithData(list);
    }
}
