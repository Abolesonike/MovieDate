package org.fizzy.moviedate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fizzy.moviedate.config.TmdbProperties;
import org.fizzy.moviedate.dto.MovieDTO;
import org.fizzy.moviedate.dto.tmdb.MovieDetailResponse;
import org.fizzy.moviedate.dto.tmdb.MovieListResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
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
     * @return 热门电影列表
     */
    public List<MovieDTO> getPopularMovies(Integer page) {
        log.debug("获取热门电影，页码：{}", page);
        
        return fetchMovieList(tmdbProperties.getBaseUrl() + "/movie/popular", page);
    }
    
    /**
     * 搜索电影
     *
     * @param query 搜索关键词
     * @param page 页码 (从 1 开始)
     * @return 搜索结果
     */
    public List<MovieDTO> searchMovies(String query, Integer page) {
        log.debug("搜索电影，关键词:{}, 页码:{}", query, page);
            
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("query", query);
        uriVariables.put("page", page.toString());
        uriVariables.put("language", "zh-CN");
        uriVariables.put("region", "CN");

        String url = tmdbProperties.getBaseUrl() + "/search/movie";
        return fetchMovieListWithBearerToken(url + "?query={query}&page={page}&language={language}&region={region}", uriVariables);
    }
    
    /**
     * 获取电影详情
     *
     * @param movieId 电影 ID
     * @return 电影详情
     */
    public MovieDTO getMovieDetail(Integer movieId) {
        log.debug("获取电影详情，ID: {}", movieId);
        
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("movieId", movieId.toString());
        uriVariables.put("language", "zh-CN");
        
        String url = tmdbProperties.getBaseUrl() + "/movie/{movieId}";
        
        MovieDetailResponse response;
        // 使用 Bearer Token 认证
        response = restClient.get()
                .uri(url + "?language={language}", uriVariables)
                .header("Authorization", "Bearer " + tmdbProperties.getKey())
                .header("accept", "application/json")
                .retrieve()
                .body(MovieDetailResponse.class);
        
        if (response == null) {
            return null;
        }
        
        return convertToMovieDTO(response);
    }
    
    /**
     * 获取正在上映的电影
     *
     * @param page 页码
     * @return 正在上映的电影列表
     */
    public List<MovieDTO> getNowPlayingMovies(Integer page) {
        log.debug("获取正在上映的电影，页码：{}", page);
        
        return fetchMovieList(tmdbProperties.getBaseUrl() + "/movie/now_playing", page);
    }
    
    /**
     * 获取即将上映的电影
     *
     * @param page 页码
     * @return 即将上映的电影列表
     */
    public List<MovieDTO> getUpcomingMovies(Integer page) {
        log.debug("获取即将上映的电影，页码：{}", page);
        
        return fetchMovieList(tmdbProperties.getBaseUrl() + "/movie/upcoming", page);
    }
    
    /**
     * 获取评分最高的电影
     *
     * @param page 页码
     * @return 评分最高的电影列表
     */
    public List<MovieDTO> getTopRatedMovies(Integer page) {
        log.debug("获取评分最高的电影，页码：{}", page);
        
        return fetchMovieList(tmdbProperties.getBaseUrl() + "/movie/top_rated", page);
    }
    
    /**
     * 获取电影列表（通用方法）
     */
    private List<MovieDTO> fetchMovieList(String baseUrl, Integer page) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("page", page.toString());
        uriVariables.put("language", "zh-CN");
        uriVariables.put("region", "CN");

        String url = baseUrl + "?page={page}&language={language}&region={region}";

        MovieListResponse response;
        // 使用 Bearer Token 认证
        response = restClient.get()
                .uri(url, uriVariables)
                .header("Authorization", "Bearer " + tmdbProperties.getKey())
                .header("accept", "application/json")
                .retrieve()
                .body(MovieListResponse.class);

        if (response == null || response.getResults() == null) {
            return List.of();
        }

        return response.getResults().stream()
                .map(this::convertToMovieDTO)
                .toList();
    }
    
    /**
     * 获取电影列表（带查询参数，使用 Bearer Token）
     */
    private List<MovieDTO> fetchMovieListWithBearerToken(String urlTemplate, Map<String, String> uriVariables) {
        MovieListResponse response = restClient.get()
                .uri(urlTemplate, uriVariables)
                .header("Authorization", "Bearer " + tmdbProperties.getKey())
                .header("accept", "application/json")
                .retrieve()
                .body(MovieListResponse.class);
        
        if (response == null || response.getResults() == null) {
            return List.of();
        }
        
        return response.getResults().stream()
                .map(this::convertToMovieDTO)
                .toList();
    }
    
    /**
     * 将 TMDB MovieResult 转换为 MovieDTO
     */
    private MovieDTO convertToMovieDTO(MovieListResponse.MovieResult result) {
        String imageUrl = tmdbProperties.getImageUrl();
        
        return MovieDTO.builder()
                .id(result.getId() != null ? result.getId().longValue() : null)
                .title(result.getTitle())
                .originalTitle(result.getOriginalTitle())
                .overview(result.getOverview())
                .posterPath(result.getPosterPath())
                .posterUrl(result.getPosterPath() != null ? imageUrl + result.getPosterPath() : null)
                .backdropPath(result.getBackdropPath())
                .backdropUrl(result.getBackdropPath() != null ? imageUrl + result.getBackdropPath() : null)
                .releaseDate(result.getReleaseDate())
                .voteAverage(result.getVoteAverage())
                .voteCount(result.getVoteCount())
                .popularity(result.getPopularity())
                .genreIds(result.getGenreIds())
                .adult(result.getAdult())
                .originalLanguage(result.getOriginalLanguage())
                .build();
    }
    
    /**
     * 将 TMDB MovieDetailResponse 转换为 MovieDTO
     */
    private MovieDTO convertToMovieDTO(MovieDetailResponse response) {
        String imageUrl = tmdbProperties.getImageUrl();
        
        return MovieDTO.builder()
                .id(response.getId() != null ? response.getId().longValue() : null)
                .title(response.getTitle())
                .originalTitle(response.getOriginalTitle())
                .overview(response.getOverview())
                .posterPath(response.getPosterPath())
                .posterUrl(response.getPosterPath() != null ? imageUrl + response.getPosterPath() : null)
                .backdropPath(response.getBackdropPath())
                .backdropUrl(response.getBackdropPath() != null ? imageUrl + response.getBackdropPath() : null)
                .releaseDate(response.getReleaseDate())
                .voteAverage(response.getVoteAverage())
                .voteCount(response.getVoteCount())
                .popularity(response.getPopularity())
                .genreIds(response.getGenreIds())
                .adult(response.getAdult())
                .originalLanguage(response.getOriginalLanguage())
                .build();
    }
}
