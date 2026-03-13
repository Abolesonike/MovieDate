package org.fizzy.moviedate.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User {
    
    /**
     * 用户 ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    
    /**
     * 密码哈希
     */
    @TableField("password_hash")
    private String passwordHash;
    
    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;
    
    /**
     * 头像 URL
     */
    @TableField("avatar_url")
    private String avatarUrl;
    
    /**
     * 个人简介
     */
    @TableField("bio")
    private String bio;
    
    /**
     * 是否激活
     */
    @TableField("is_active")
    @Builder.Default
    private Boolean isActive = true;
    
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
