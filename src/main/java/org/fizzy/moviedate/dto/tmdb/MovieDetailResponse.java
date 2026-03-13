package org.fizzy.moviedate.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TMDB 电影详情响应 DTO
 */
@Data
public class MovieDetailResponse {
    
    private Integer id;
    private String title;
    private String overview;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("vote_average")
    private Double voteAverage;
    @JsonProperty("vote_count")
    private Integer voteCount;
    @JsonProperty("popularity")
    private Double popularity;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    private Boolean adult;
    private String originalLanguage;
    @JsonProperty("original_title")
    private String originalTitle;
    private String status;
    private Integer runtime;
    @JsonProperty("budget")
    private Integer budget;
    @JsonProperty("revenue")
    private Integer revenue;
}
