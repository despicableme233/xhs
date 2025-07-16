package com.quanxiaoha.xiaohashu.oss.biz.factory;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.quanxiaoha.xiaohashu.oss.biz.strategy.FileStrategy;
import com.quanxiaoha.xiaohashu.oss.biz.strategy.impl.AliyunOSSFileStrategy;
import com.quanxiaoha.xiaohashu.oss.biz.strategy.impl.MinioFileStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Slf4j
@Configuration
@Lazy
public class FileStrategyFactory {

    @NacosValue(value = "${storage.type}", autoRefreshed = true)
    private String fileStrategy;

    @Bean(value = "fileStrategy")
    @Lazy
    public FileStrategy getFileStrategy() throws IllegalAccessException {
        if (fileStrategy == null) {
            throw new IllegalStateException("storage.type is not configured in Nacos");
        }
        log.info("文件策略是{}", fileStrategy);
        return switch (fileStrategy) {
            case "aliyun" -> new AliyunOSSFileStrategy();
            case "minio" -> new MinioFileStrategy();
            default -> throw new IllegalAccessException("not supported file strategy");
        };
    }
}
