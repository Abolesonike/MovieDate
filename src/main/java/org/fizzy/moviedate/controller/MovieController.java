package org.fizzy.moviedate.controller;

import lombok.RequiredArgsConstructor;
import org.fizzy.moviedate.dto.MovieDTO;
import org.fizzy.moviedate.service.TmdbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * @return 热门电影列表
     */
    @GetMapping("/popular")
    public ResponseEntity<List<MovieDTO>> getPopularMovies(
            @RequestParam(defaultValue = "1") Integer page) {
        List<MovieDTO> movies = tmdbService.getPopularMovies(page);
        return ResponseEntity.ok(movies);
    }
    
    /**
     * 搜索电影
     *
     * @param query 搜索关键词
     * @param page 页码 (默认 1)
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") Integer page) {
        List<MovieDTO> movies = tmdbService.searchMovies(query, page);
        return ResponseEntity.ok(movies);
    }
    
    /**
     * 获取电影详情
     *
     * @param movieId 电影 ID
     * @return 电影详情
     */
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDTO> getMovieDetail(@PathVariable Integer movieId) {
        MovieDTO movie = tmdbService.getMovieDetail(movieId);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }
    
    /**
     * 获取正在上映的电影
     *
     * @param page 页码 (默认 1)
     * @return 正在上映的电影列表
     */
    @GetMapping("/now-playing")
    public ResponseEntity<List<MovieDTO>> getNowPlayingMovies(
            @RequestParam(defaultValue = "1") Integer page) {
        List<MovieDTO> movies = tmdbService.getNowPlayingMovies(page);
        return ResponseEntity.ok(movies);
    }
    
    /**
     * 获取即将上映的电影
     *
     * @param page 页码 (默认 1)
     * @return 即将上映的电影列表
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<MovieDTO>> getUpcomingMovies(
            @RequestParam(defaultValue = "1") Integer page) {
        List<MovieDTO> movies = tmdbService.getUpcomingMovies(page);
        return ResponseEntity.ok(movies);
    }
    
    /**
     * 获取评分最高的电影
     *
     * @param page 页码 (默认 1)
     * @return 评分最高的电影列表
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<MovieDTO>> getTopRatedMovies(
            @RequestParam(defaultValue = "1") Integer page) {
        List<MovieDTO> movies = tmdbService.getTopRatedMovies(page);
        return ResponseEntity.ok(movies);
    }
}
