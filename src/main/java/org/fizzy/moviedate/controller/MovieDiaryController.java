package org.fizzy.moviedate.controller;

import lombok.RequiredArgsConstructor;
import org.fizzy.moviedate.dto.MovieDiaryDTO;
import org.fizzy.moviedate.service.MovieDiaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 电影日记 API 控制器
 */
@RestController
@RequestMapping("/api/movie-diary")
@RequiredArgsConstructor
public class MovieDiaryController {
    
    private final MovieDiaryService movieDiaryService;
    
    /**
     * 创建电影日记
     *
     * @param userId 用户 ID（从 Header 中获取）
     * @param dto    电影日记 DTO
     * @return 创建后的电影日记
     */
    @PostMapping
    public ResponseEntity<MovieDiaryDTO> create(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody MovieDiaryDTO dto) {
        MovieDiaryDTO created = movieDiaryService.create(userId, dto);
        return ResponseEntity.ok(created);
    }
    
    /**
     * 根据 ID 删除电影日记
     *
     * @param id     电影日记 ID
     * @param userId 用户 ID（从 Header 中获取）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        movieDiaryService.deleteById(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 更新电影日记
     *
     * @param id     电影日记 ID
     * @param userId 用户 ID（从 Header 中获取）
     * @param dto    更新内容
     * @return 更新后的电影日记
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovieDiaryDTO> update(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody MovieDiaryDTO dto) {
        MovieDiaryDTO updated = movieDiaryService.update(id, userId, dto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * 根据 ID 查询电影日记
     *
     * @param id     电影日记 ID
     * @param userId 用户 ID（从 Header 中获取）
     * @return 电影日记详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovieDiaryDTO> getById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        MovieDiaryDTO diary = movieDiaryService.getById(id, userId);
        if (diary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(diary);
    }
    
    /**
     * 分页查询当前用户的电影日记
     *
     * @param userId 用户 ID（从 Header 中获取）
     * @param page   页码 (默认 1)
     * @param size   每页大小 (默认 10)
     * @return 电影日记列表
     */
    @GetMapping
    public ResponseEntity<List<MovieDiaryDTO>> list(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        List<MovieDiaryDTO> list = movieDiaryService.listByUser(userId, page, size);
        return ResponseEntity.ok(list);
    }
    
    /**
     * 根据 TMDB 电影 ID 查询当前用户的电影日记
     *
     * @param userId      用户 ID（从 Header 中获取）
     * @param tmdbMovieId TMDB 电影 ID
     * @return 电影日记列表
     */
    @GetMapping("/tmdb/{tmdbMovieId}")
    public ResponseEntity<List<MovieDiaryDTO>> listByTmdbMovieId(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Integer tmdbMovieId) {
        List<MovieDiaryDTO> list = movieDiaryService.listByTmdbMovieId(userId, tmdbMovieId);
        return ResponseEntity.ok(list);
    }
    
    /**
     * 根据日期范围查询当前用户的电影日记
     *
     * @param userId    用户 ID（从 Header 中获取）
     * @param startDate 开始日期 (格式：yyyy-MM-dd)
     * @param endDate   结束日期 (格式：yyyy-MM-dd)
     * @return 电影日记列表
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<MovieDiaryDTO>> listByDateRange(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<MovieDiaryDTO> list = movieDiaryService.listByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(list);
    }
    
    /**
     * 查询当前用户收藏的电影日记
     *
     * @param userId 用户 ID（从 Header 中获取）
     * @return 收藏的电影日记列表
     */
    @GetMapping("/favorites")
    public ResponseEntity<List<MovieDiaryDTO>> listFavorites(
            @RequestHeader("X-User-Id") Long userId) {
        List<MovieDiaryDTO> list = movieDiaryService.listFavorites(userId);
        return ResponseEntity.ok(list);
    }
    
    /**
     * 统计当前用户的电影总数
     *
     * @param userId 用户 ID（从 Header 中获取）
     * @return 电影总数
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count(
            @RequestHeader("X-User-Id") Long userId) {
        long count = movieDiaryService.countByUser(userId);
        return ResponseEntity.ok(count);
    }
}
