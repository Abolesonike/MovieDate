package org.fizzy.moviedate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tmdb.api")
public class TmdbProperties {
    
    /**
     * TMDB API 基础 URL
     */
    private String baseUrl;
    
    /**
     * TMDB API Key (v3 auth)
     */
    private String key;
    
    /**
     * 图片基础 URL
     */
    private String imageUrl;
    
    /**
     * 读取超时 (毫秒)
     */
    private Integer readTimeout;
    
    /**
     * 连接超时 (毫秒)
     */
    private Integer connectTimeout;
}
