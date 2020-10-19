package com.xiao.testspring.controller;

import com.xiao.testspring.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述:
 *
 * @Params:
 * @Author: LiuMZ
 * @Date: 2020/10/19 11:05
 */
@RestController
@RequestMapping("/thread/pool")
public class threadPoolController {

    @ResponseBody
    @RequestMapping("/test")
    public Result test(){
        Result res=new Result("result测试");
        return res;
    }



}
