package org.fizzy.moviedate.controller;

import lombok.RequiredArgsConstructor;
import org.fizzy.moviedate.service.TmdbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 电影 API 控制器
 */
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    
    private final TmdbService tmdbService;
    
    /**
     * 获取热门电影列表
     *
     * @param page 页码 (默认 1)
     * @return 热门电影列表（原始 JSON 数据）
     */
    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularMovies(
            @RequestParam(defaultValue = "1") Integer page) {
        Map<String, Object> response = tmdbService.getPopularMovies(page);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 搜索电影
     *
     * @param query 搜索关键词
     * @param page 页码 (默认 1)
     * @return 搜索结果（原始 JSON 数据）
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") Integer page) {
        Map<String, Object> response = tmdbService.searchMovies(query, page);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取电影详情
     *
     * @param movieId 电影 ID
     * @return 电影详情（原始 JSON 数据）
     */
    @GetMapping("/{movieId}")
    public ResponseEntity<Map<String, Object>> getMovieDetail(@PathVariable Integer movieId) {
        Map<String, Object> movie = tmdbService.getMovieDetail(movieId);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }
    
    /**
     * 获取正在上映的电影
     *
     * @param page 页码 (默认 1)
     * @return 正在上映的电影列表（原始 JSON 数据）
     */
    @GetMapping("/now-playing")
    public ResponseEntity<Map<String, Object>> getNowPlayingMovies(
            @RequestParam(defaultValue = "1") Integer page) {
        Map<String, Object> response = tmdbService.getNowPlayingMovies(page);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取即将上映的电影
     *
     * @param page 页码 (默认 1)
     * @return 即将上映的电影列表（原始 JSON 数据）
     */
    @GetMapping("/upcoming")
    public ResponseEntity<Map<String, Object>> getUpcomingMovies(
            @RequestParam(defaultValue = "1") Integer page) {
        Map<String, Object> response = tmdbService.getUpcomingMovies(page);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取评分最高的电影
     *
     * @param page 页码 (默认 1)
     * @return 评分最高的电影列表（原始 JSON 数据）
     */
    @GetMapping("/top-rated")
    public ResponseEntity<Map<String, Object>> getTopRatedMovies(
            @RequestParam(defaultValue = "1") Integer page) {
        Map<String, Object> response = tmdbService.getTopRatedMovies(page);
        return ResponseEntity.ok(response);
    }
}
