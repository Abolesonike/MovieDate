package org.fizzy.moviedate.exception;

/**
 * TMDB API 异常
 */
public class TmdbApiException extends RuntimeException {
    
    public TmdbApiException(String message) {
        super(message);
    }
    
    public TmdbApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
