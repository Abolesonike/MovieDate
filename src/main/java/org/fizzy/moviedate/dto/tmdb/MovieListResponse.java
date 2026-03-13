package org.fizzy.moviedate.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TMDB 电影列表响应 DTO
 */
@Data
public class MovieListResponse {
    
    private Integer page;
    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonProperty("total_results")
    private Integer totalResults;
    private List<MovieResult> results;
    
    @Data
    public static class MovieResult {
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
    }
}
