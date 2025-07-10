package com.quanxiaoha.xiaohashu.auth.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.quanxiaoha.xiaohashu.auth.alarm.AlarmInterface;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @NacosValue(value = "${rate-limit.api.limit}", autoRefreshed = true)
    private Integer limit;

    @Resource
    private AlarmInterface alarm;

    @GetMapping("/test")
    public String test() {
        return "限流值为：" + limit.toString();
    }

    @GetMapping("/alarm")
    public String sendAlarm() {
    alarm.send("出错");
    return "alarm send";
    }
}
