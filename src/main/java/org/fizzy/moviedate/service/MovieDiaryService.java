package org.fizzy.moviedate.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fizzy.moviedate.dto.MovieDiaryDTO;
import org.fizzy.moviedate.entity.MovieDiary;
import org.fizzy.moviedate.mapper.MovieDiaryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 电影日记服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MovieDiaryService {
    
    private final MovieDiaryMapper movieDiaryMapper;
    
    /**
     * 创建电影日记
     *
     * @param userId 用户 ID
     * @param dto    电影日记 DTO
     * @return 创建后的电影日记
     */
    @Transactional
    public MovieDiaryDTO create(Long userId, MovieDiaryDTO dto) {
        log.info("用户 {} 创建电影日记：{}", userId, dto.getMovieTitle());
        
        MovieDiary movieDiary = MovieDiary.builder()
                .userId(userId)
                .tmdbMovieId(dto.getTmdbMovieId())
                .movieTitle(dto.getMovieTitle())
                .watchDate(dto.getWatchDate())
                .rating(dto.getRating())
                .review(dto.getReview())
                .posterUrl(dto.getPosterUrl())
                .isFavorite(dto.getIsFavorite() != null ? dto.getIsFavorite() : false)
                .build();
        
        movieDiaryMapper.insert(movieDiary);
        
        return convertToDTO(movieDiary);
    }
    
    /**
     * 根据 ID 删除电影日记（验证用户权限）
     *
     * @param id     电影日记 ID
     * @param userId 用户 ID
     */
    @Transactional
    public void deleteById(Long id, Long userId) {
        log.info("删除电影日记，ID: {}, 用户：{}", id, userId);
        
        MovieDiary existing = movieDiaryMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("电影日记不存在，ID: " + id);
        }
        
        // 验证是否是该用户的日记
        if (!existing.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除他人的电影日记");
        }
        
        movieDiaryMapper.deleteById(id);
    }
    
    /**
     * 更新电影日记（验证用户权限）
     *
     * @param id     电影日记 ID
     * @param userId 用户 ID
     * @param dto    更新内容
     * @return 更新后的电影日记
     */
    @Transactional
    public MovieDiaryDTO update(Long id, Long userId, MovieDiaryDTO dto) {
        log.info("更新电影日记，ID: {}, 用户：{}", id, userId);
        
        MovieDiary existing = movieDiaryMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("电影日记不存在，ID: " + id);
        }
        
        // 验证是否是该用户的日记
        if (!existing.getUserId().equals(userId)) {
            throw new RuntimeException("无权更新他人的电影日记");
        }
        
        existing.setMovieTitle(dto.getMovieTitle());
        existing.setWatchDate(dto.getWatchDate());
        existing.setRating(dto.getRating());
        existing.setReview(dto.getReview());
        existing.setPosterUrl(dto.getPosterUrl());
        existing.setIsFavorite(dto.getIsFavorite() != null ? dto.getIsFavorite() : existing.getIsFavorite());
        
        movieDiaryMapper.updateById(existing);
        
        return convertToDTO(existing);
    }
    
    /**
     * 根据 ID 查询电影日记（验证用户权限）
     *
     * @param id     电影日记 ID
     * @param userId 用户 ID
     * @return 电影日记详情
     */
    public MovieDiaryDTO getById(Long id, Long userId) {
        log.debug("查询电影日记，ID: {}, 用户：{}", id, userId);
        
        MovieDiary movieDiary = movieDiaryMapper.selectById(id);
        
        if (movieDiary != null && !movieDiary.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看他人的电影日记");
        }
        
        return movieDiary != null ? convertToDTO(movieDiary) : null;
    }
    
    /**
     * 分页查询当前用户的电影日记
     *
     * @param userId 用户 ID
     * @param page   页码 (从 1 开始)
     * @param size   每页大小
     * @return 电影日记列表
     */
    public List<MovieDiaryDTO> listByUser(Long userId, Integer page, Integer size) {
        log.debug("查询用户 {} 的电影日记，页码：{}, 大小：{}", userId, page, size);
        
        Page<MovieDiary> mpPage = new Page<>(page, size);
        LambdaQueryWrapper<MovieDiary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MovieDiary::getUserId, userId)
                .orderByDesc(MovieDiary::getWatchDate);
        
        Page<MovieDiary> result = movieDiaryMapper.selectPage(mpPage, wrapper);
        
        return result.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据 TMDB 电影 ID 查询当前用户的电影日记
     *
     * @param userId      用户 ID
     * @param tmdbMovieId TMDB 电影 ID
     * @return 电影日记列表
     */
    public List<MovieDiaryDTO> listByTmdbMovieId(Long userId, Integer tmdbMovieId) {
        log.debug("用户 {} 根据 TMDB 电影 ID 查询，TMDB ID: {}", userId, tmdbMovieId);
        
        LambdaQueryWrapper<MovieDiary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MovieDiary::getUserId, userId)
                .eq(MovieDiary::getTmdbMovieId, tmdbMovieId)
                .orderByDesc(MovieDiary::getWatchDate);
        
        List<MovieDiary> list = movieDiaryMapper.selectList(wrapper);
        
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据日期范围查询当前用户的电影日记
     *
     * @param userId    用户 ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 电影日记列表
     */
    public List<MovieDiaryDTO> listByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        log.debug("用户 {} 根据日期范围查询，开始：{}, 结束：{}", userId, startDate, endDate);
        
        LambdaQueryWrapper<MovieDiary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MovieDiary::getUserId, userId)
                .between(MovieDiary::getWatchDate, startDate, endDate)
                .orderByDesc(MovieDiary::getWatchDate);
        
        List<MovieDiary> list = movieDiaryMapper.selectList(wrapper);
        
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询当前用户收藏的电影日记
     *
     * @param userId 用户 ID
     * @return 收藏的电影日记列表
     */
    public List<MovieDiaryDTO> listFavorites(Long userId) {
        log.debug("查询用户 {} 收藏的电影日记", userId);
        
        LambdaQueryWrapper<MovieDiary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MovieDiary::getUserId, userId)
                .eq(MovieDiary::getIsFavorite, true)
                .orderByDesc(MovieDiary::getWatchDate);
        
        List<MovieDiary> list = movieDiaryMapper.selectList(wrapper);
        
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 统计当前用户的电影总数
     *
     * @param userId 用户 ID
     * @return 电影总数
     */
    public long countByUser(Long userId) {
        LambdaQueryWrapper<MovieDiary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MovieDiary::getUserId, userId);
        return movieDiaryMapper.selectCount(wrapper);
    }
    
    /**
     * 将实体转换为 DTO
     */
    private MovieDiaryDTO convertToDTO(MovieDiary movieDiary) {
        return MovieDiaryDTO.builder()
                .id(movieDiary.getId())
                .userId(movieDiary.getUserId())
                .tmdbMovieId(movieDiary.getTmdbMovieId())
                .movieTitle(movieDiary.getMovieTitle())
                .watchDate(movieDiary.getWatchDate())
                .rating(movieDiary.getRating())
                .review(movieDiary.getReview())
                .posterUrl(movieDiary.getPosterUrl())
                .isFavorite(movieDiary.getIsFavorite())
                .createdAt(movieDiary.getCreatedAt())
                .updatedAt(movieDiary.getUpdatedAt())
                .build();
    }
}
