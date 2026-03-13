package org.fizzy.moviedate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * REST 客户端配置
 */
@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    
    private final TmdbProperties tmdbProperties;
    
    /**
     * 配置 RestClient
     */
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .requestFactory(createRequestFactory())
                .build();
    }
    
    /**
     * 创建请求工厂，配置超时时间
     */
    private ClientHttpRequestFactory createRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(tmdbProperties.getReadTimeout());
        factory.setConnectTimeout(tmdbProperties.getConnectTimeout());
        return factory;
    }
}
