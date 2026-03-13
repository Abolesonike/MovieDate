package org.fizzy.moviedate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 电影日记 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDiaryDTO {
    
    /**
     * 主键 ID
     */
    private Long id;
    
    /**
     * 用户 ID
     */
    private Long userId;
    
    /**
     * TMDB 电影 ID
     */
    private Integer tmdbMovieId;
    
    /**
     * 电影标题
     */
    private String movieTitle;
    
    /**
     * 观影日期
     */
    private LocalDate watchDate;
    
    /**
     * 评分 (0-10)
     */
    private BigDecimal rating;
    
    /**
     * 观影心得/评论
     */
    private String review;
    
    /**
     * 电影海报 URL
     */
    private String posterUrl;
    
    /**
     * 是否收藏
     */
    private Boolean isFavorite;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
