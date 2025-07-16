package com.quanxiaoha.xiaohashu.oss.biz.strategy.impl;

import com.quanxiaoha.xiaohashu.oss.biz.config.MinioProperties;
import com.quanxiaoha.xiaohashu.oss.biz.strategy.FileStrategy;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
public class MinioFileStrategy implements FileStrategy {

    @Resource
    private  MinioClient minioClient;

    @Resource
    private  MinioProperties minioProperties;

    @SneakyThrows
    @Override
    public String upload(MultipartFile file, String bucketName) {
        //文件判空
        if (file == null || file.getSize() == 0) {
            log.error("minio file is null or empty");
            throw new RuntimeException("文件大小不能为空");
        }
        //文件原始名
        String originalFilename = file.getOriginalFilename();
        //文件contentType
        String contentType = file.getContentType();
        //获取存取对象名称
        String key = UUID.randomUUID().toString().replace("-", "");
        //文件名后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = String.format("%s.%s", key, suffix);
        log.info("minio file uploaded starts...");
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(newFileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(contentType)
                .build());

        //文件访问链接
        String fileUrl=String.format("%s/%s/%s", minioProperties.getEndpoint(),bucketName, newFileName);
        return "";
    }
}
