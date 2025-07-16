package com.quanxiaoha.xiaohashu.oss.biz.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件传输顶级接口
 * 采取策略模式
 */
public interface FileStrategy {
    /**
     * 文件上传接口
     * @param file
     * @param bucketName
     * @return
     */
     String upload(MultipartFile file, String bucketName);
}
