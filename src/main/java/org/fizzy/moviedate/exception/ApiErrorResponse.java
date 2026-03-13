package org.fizzy.moviedate.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * API 错误响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    
    /**
     * HTTP 状态码
     */
    private Integer code;
    
    /**
     * 错误消息
     */
    private String message;
    
    /**
     * 错误描述
     */
    private String description;
    
    /**
     * 时间戳
     */
    private Long timestamp;
}
