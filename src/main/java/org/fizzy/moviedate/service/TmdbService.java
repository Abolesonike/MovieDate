package org.fizzy.moviedate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fizzy.moviedate.config.TmdbProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * TMDB 电影服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TmdbService {
    
    private final TmdbProperties tmdbProperties;
    private final RestClient restClient;
    
    /**
     * 获取热门电影列表
     *
     * @param page 页码 (从 1 开始)
     * @return 热门电影列表（原始 Map 数据）
     */
    public Map<String, Object> getPopularMovies(Integer page) {
        log.debug("获取热门电影，页码：{}", page);
        
        return fetchMovieData(tmdbProperties.getBaseUrl() + "/movie/popular", page);
    }
    
    /**
     * 搜索电影
     *
     * @param query 搜索关键词
     * @param page 页码 (从 1 开始)
     * @return 搜索结果（原始 Map 数据）
     */
    public Map<String, Object> searchMovies(String query, Integer page) {
        log.debug("搜索电影，关键词:{}, 页码:{}", query, page);
                
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("query", query);
        uriVariables.put("page", page.toString());
        uriVariables.put("language", "zh-CN");
        uriVariables.put("region", "CN");
                
        String url = tmdbProperties.getBaseUrl() + "/search/movie";
        return fetchMovieDataWithBearerToken(url + "?query={query}&page={page}&language={language}&region={region}", uriVariables);
    }
    
    /**
     * 获取电影详情
     *
     * @param movieId 电影 ID
     * @return 电影详情（原始 Map 数据）
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMovieDetail(Integer movieId) {
        log.debug("获取电影详情，ID: {}", movieId);
        
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("movieId", movieId.toString());
        uriVariables.put("language", "zh-CN");
        
        String url = tmdbProperties.getBaseUrl() + "/movie/{movieId}?language={language}";
        
        // 使用 Bearer Token 认证
        return (Map<String, Object>) restClient.get()
                .uri(url, uriVariables)
                .header("Authorization", "Bearer " + tmdbProperties.getKey())
                .header("accept", "application/json")
                .retrieve()
                .body(Map.class);
    }
    
    /**
     * 获取正在上映的电影
     *
     * @param page 页码
     * @return 正在上映的电影列表（原始 Map 数据）
     */
    public Map<String, Object> getNowPlayingMovies(Integer page) {
        log.debug("获取正在上映的电影，页码：{}", page);
        
        return fetchMovieData(tmdbProperties.getBaseUrl() + "/movie/now_playing", page);
    }
    
    /**
     * 获取即将上映的电影
     *
     * @param page 页码
     * @return 即将上映的电影列表（原始 Map 数据）
     */
    public Map<String, Object> getUpcomingMovies(Integer page) {
        log.debug("获取即将上映的电影，页码：{}", page);
        
        return fetchMovieData(tmdbProperties.getBaseUrl() + "/movie/upcoming", page);
    }
    
    /**
     * 获取评分最高的电影
     *
     * @param page 页码
     * @return 评分最高的电影列表（原始 Map 数据）
     */
    public Map<String, Object> getTopRatedMovies(Integer page) {
        log.debug("获取评分最高的电影，页码：{}", page);
        
        return fetchMovieData(tmdbProperties.getBaseUrl() + "/movie/top_rated", page);
    }
    
    /**
     * 获取电影数据（通用方法，返回原始 Map）
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> fetchMovieData(String baseUrl, Integer page) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("page", page.toString());
        uriVariables.put("language", "zh-CN");
        uriVariables.put("region", "CN");

        String url = baseUrl + "?page={page}&language={language}&region={region}";

        // 使用 Bearer Token 认证
        return (Map<String, Object>) restClient.get()
                .uri(url, uriVariables)
                .header("Authorization", "Bearer " + tmdbProperties.getKey())
                .header("accept", "application/json")
                .retrieve()
                .body(Map.class);
    }
    
    /**
     * 获取电影数据（带查询参数，使用 Bearer Token，返回原始 Map）
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> fetchMovieDataWithBearerToken(String urlTemplate, Map<String, String> uriVariables) {
        // 使用 Bearer Token 认证
        return (Map<String, Object>) restClient.get()
                .uri(urlTemplate, uriVariables)
                .header("Authorization", "Bearer " + tmdbProperties.getKey())
                .header("accept", "application/json")
                .retrieve()
                .body(Map.class);
    }

}
