package org.fizzy.moviedate.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fizzy.moviedate.dto.*;
import org.fizzy.moviedate.entity.User;
import org.fizzy.moviedate.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

/**
 * 用户服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Value("${jwt.secret:mySecretKeyForJWTTokenGeneration}")
    private String jwtSecret;
    
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 用户信息
     */
    @Transactional
    public AuthResponseDTO register(UserRegisterDTO registerDTO) {
        log.info("用户注册：{}", registerDTO.getUsername());
        
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        User existingUser = userMapper.selectOne(wrapper);
        
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, registerDTO.getEmail());
        existingUser = userMapper.selectOne(wrapper);
        
        if (existingUser != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        // 创建用户
        User user = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .passwordHash(passwordEncoder.encode(registerDTO.getPassword()))
                .nickname(registerDTO.getNickname())
                .isActive(true)
                .build();
        
        userMapper.insert(user);
        
        // 生成 token
        String token = generateToken(user);
        
        return AuthResponseDTO.builder()
                .user(convertToDTO(user))
                .token(token)
                .build();
    }
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 认证响应
     */
    public AuthResponseDTO login(UserLoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO.getUsernameOrEmail());
        
        // 根据用户名或邮箱查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(User::getUsername, loginDTO.getUsernameOrEmail())
                        .or().eq(User::getEmail, loginDTO.getUsernameOrEmail()));
        
        User user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("密码错误");
        }
        
        // 检查用户是否被禁用
        if (!user.getIsActive()) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // 生成 token
        String token = generateToken(user);
        
        return AuthResponseDTO.builder()
                .user(convertToDTO(user))
                .token(token)
                .build();
    }
    
    /**
     * 根据 ID 获取用户
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    public UserDTO getById(Long userId) {
        log.debug("查询用户，ID: {}", userId);
        
        User user = userMapper.selectById(userId);
        
        return user != null ? convertToDTO(user) : null;
    }
    
    /**
     * 更新用户信息
     *
     * @param userId 用户 ID
     * @param dto    更新内容
     * @return 更新后的用户
     */
    @Transactional
    public UserDTO update(Long userId, UserDTO dto) {
        log.info("更新用户信息，ID: {}", userId);
        
        User existing = userMapper.selectById(userId);
        if (existing == null) {
            throw new RuntimeException("用户不存在");
        }
        
        existing.setNickname(dto.getNickname());
        existing.setBio(dto.getBio());
        existing.setAvatarUrl(dto.getAvatarUrl());
        
        userMapper.updateById(existing);
        
        return convertToDTO(existing);
    }
    
    /**
     * 修改密码
     *
     * @param userId    用户 ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("修改密码，ID: {}", userId);
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new RuntimeException("旧密码错误");
        }
        
        // 更新密码
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }
    
    /**
     * 将实体转换为 DTO
     */
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    
    /**
     * 生成简单的 Token（生产环境建议使用 JWT）
     */
    private String generateToken(User user) {
        try {
            String data = user.getId() + ":" + user.getUsername() + ":" + System.currentTimeMillis();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成 token 失败", e);
        }
    }
}
