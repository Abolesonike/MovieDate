package org.fizzy.moviedate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 电影信息 DTO (简化版，用于 API 响应)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    
    /**
     * 电影 ID
     */
    private Long id;
    
    /**
     * 电影标题
     */
    private String title;
    
    /**
     * 原始标题
     */
    private String originalTitle;
    
    /**
     * 概述/简介
     */
    private String overview;
    
    /**
     * 海报路径
     */
    private String posterPath;
    
    /**
     * 完整海报 URL
     */
    private String posterUrl;
    
    /**
     * 背景图路径
     */
    private String backdropPath;
    
    /**
     * 完整背景图 URL
     */
    private String backdropUrl;
    
    /**
     * 上映日期
     */
    private String releaseDate;
    
    /**
     * 评分
     */
    private Double voteAverage;
    
    /**
     * 评分人数
     */
    private Integer voteCount;
    
    /**
     * 热度
     */
    private Double popularity;
    
    /**
     * 类型 ID 列表
     */
    private List<Integer> genreIds;
    
    /**
     * 是否成人内容
     */
    private Boolean adult;
    
    /**
     * 原始语言
     */
    private String originalLanguage;
}
