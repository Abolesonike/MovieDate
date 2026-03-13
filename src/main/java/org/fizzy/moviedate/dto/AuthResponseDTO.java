package org.fizzy.moviedate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    
    /**
     * 用户信息
     */
    private UserDTO user;
    
    /**
     * Token
     */
    private String token;
    
    /**
     * Token 类型
     */
    @Builder.Default
    private String tokenType = "Bearer";
}
