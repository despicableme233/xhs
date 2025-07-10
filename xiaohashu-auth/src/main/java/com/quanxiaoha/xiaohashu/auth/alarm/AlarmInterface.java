package com.quanxiaoha.xiaohashu.auth.alarm;

public interface AlarmInterface {
    /**
     * 发送警告信息
     * @param message 发送警告信息
     * @return
     */
    boolean send(String message);
}
