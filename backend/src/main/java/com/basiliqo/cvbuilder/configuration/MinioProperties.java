package com.basiliqo.cvbuilder.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    @Value("url")
    private String url;

    @Value("accesskey")
    private String accessKey;

    @Value("secretkey")
    private String secretKey;

    @Value("templates-bucket")
    private String templatesBucket;

    @Value("documents-bucket")
    private String documentsBucket;
}
