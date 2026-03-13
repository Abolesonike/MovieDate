package org.fizzy.moviedate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理 TMDB API 异常
     */
    @ExceptionHandler(TmdbApiException.class)
    public ResponseEntity<ApiErrorResponse> handleTmdbApiException(TmdbApiException ex) {
        log.error("TMDB API 错误：{}", ex.getMessage());
        
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(HttpStatus.BAD_GATEWAY.value())
                .message("TMDB API 请求失败")
                .description(ex.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }
    
    /**
     * 处理资源访问异常（网络问题）
     */
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceAccessException(ResourceAccessException ex) {
        log.error("资源访问错误：{}", ex.getMessage());
        
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("无法连接到 TMDB 服务")
                .description("请稍后重试或检查网络连接")
                .timestamp(System.currentTimeMillis())
                .build();
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        log.error("未知错误：{}", ex.getMessage(), ex);
        
        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("服务器内部错误")
                .description(ex.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
