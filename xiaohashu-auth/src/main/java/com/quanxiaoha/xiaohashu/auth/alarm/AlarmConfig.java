package com.quanxiaoha.xiaohashu.auth.alarm;

import com.quanxiaoha.xiaohashu.auth.alarm.impl.MailAlarmHelper;
import com.quanxiaoha.xiaohashu.auth.alarm.impl.SmsAlarmHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class AlarmConfig {
    @Value("${alarm.type}")
    private String alarmType;

    @Bean
    @RefreshScope
    public AlarmInterface alarmHelper() {
        //根据配置文件中的类型，初始化不同通知类型类
        if (alarmType.equals("sms")) {
            return new SmsAlarmHelper();
        } else if (alarmType.equals("mail")) {
            return new MailAlarmHelper();
        } else {
            throw new IllegalArgumentException("错误的警告类型");
        }
    }
}
