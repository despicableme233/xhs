package com.quanxiaoha.xiaohashu.oss.biz.strategy.impl;

import com.quanxiaoha.xiaohashu.oss.biz.strategy.FileStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云文件上传方案实现
 */
@Slf4j
public class AliyunOSSFileStrategy implements FileStrategy {

    @Override
    public String upload(MultipartFile file, String bucketName) {
        //todo 阿里云对象存储服务
        log.info("aliyunOSSFileStrategy uploaded starts...");
        return "";
    }
}
