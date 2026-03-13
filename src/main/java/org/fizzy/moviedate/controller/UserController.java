package org.fizzy.moviedate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fizzy.moviedate.dto.*;
import org.fizzy.moviedate.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户 API 控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 认证响应
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        AuthResponseDTO response = userService.register(registerDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 认证响应
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        AuthResponseDTO response = userService.login(loginDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取当前用户信息
     *
     * @param userId 用户 ID（从 token 中解析）
     * @return 用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("X-User-Id") Long userId) {
        UserDTO user = userService.getById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
    /**
     * 更新用户信息
     *
     * @param userId 用户 ID
     * @param dto    更新内容
     * @return 更新后的用户
     */
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody UserDTO dto) {
        UserDTO updated = userService.update(userId, dto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * 修改密码
     *
     * @param userId 用户 ID
     * @param dto    密码信息
     */
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody PasswordChangeDTO dto) {
        userService.changePassword(userId, dto.getOldPassword(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
    
    /**
     * 密码修改 DTO
     */
    public static class PasswordChangeDTO {
        private String oldPassword;
        private String newPassword;
        
        public String getOldPassword() {
            return oldPassword;
        }
        
        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
