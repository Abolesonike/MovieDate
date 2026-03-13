package org.fizzy.moviedate.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 电影日记实体类（观影日历）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("movie_diary")
public class MovieDiary {
    
    /**
     * 主键 ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户 ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * TMDB 电影 ID
     */
    @TableField("tmdb_movie_id")
    private Integer tmdbMovieId;
    
    /**
     * 电影标题
     */
    @TableField("movie_title")
    private String movieTitle;
    
    /**
     * 观影日期
     */
    @TableField("watch_date")
    private LocalDate watchDate;
    
    /**
     * 评分 (0-10)
     */
    @TableField("rating")
    private BigDecimal rating;
    
    /**
     * 观影心得/评论
     */
    @TableField("review")
    private String review;
    
    /**
     * 电影海报 URL
     */
    @TableField("poster_url")
    private String posterUrl;
    
    /**
     * 是否收藏
     */
    @TableField("is_favorite")
    @Builder.Default
    private Boolean isFavorite = false;
    
    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
