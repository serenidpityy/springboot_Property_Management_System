package com.wfit.springbootbookstores.controller;

import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // 验证必要字段
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户名不能为空");
            }
            if (user.getName() == null || user.getName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("姓名不能为空");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("密码不能为空");
            }
            if (user.getUserType() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户类型不能为空");
            }
            
            if (userService.existsByUsername(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("用户名已存在");
            }
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("注册失败: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器内部错误: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        User.UserType userType = User.UserType.valueOf(loginRequest.get("userType").toUpperCase());

        Optional<User> userOptional = userService.loginUser(username, password, userType);
        if (userOptional.isPresent()) {
            // In a real application, you would generate a JWT or session token here
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials or user type");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User user) {
        Optional<User> existingUserOptional = userService.getUserById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            
            // Check if username is being changed and if it already exists
            if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
                if (userService.existsByUsername(user.getUsername())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
                }
                existingUser.setUsername(user.getUsername());
            }
            
            // Update other allowed fields
            if (user.getName() != null) {
                existingUser.setName(user.getName());
            }
            if (user.getPhone() != null) {
                existingUser.setPhone(user.getPhone());
            }
            if (user.getDisplayName() != null) {
                existingUser.setDisplayName(user.getDisplayName());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }
            
            // You might want to prevent user_type changes via this endpoint

            User updatedUser = userService.updateUser(existingUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/type/{userType}")
    public ResponseEntity<List<User>> getUsersByUserType(@PathVariable User.UserType userType) {
        List<User> users = userService.getUsersByUserType(userType);
        return ResponseEntity.ok(users);
    }

    /**
     * 更新用户账户余额
     * @param id 用户ID
     * @param request 包含新余额的请求体 {"amount": 新余额值}
     * @return 更新后的用户信息
     */
    @PutMapping("/{id}/balance")
    public ResponseEntity<?> updateAccountBalance(@PathVariable Integer id, @RequestBody Map<String, BigDecimal> request) {
        BigDecimal newBalance = request.get("amount");
        if (newBalance == null) {
            return ResponseEntity.badRequest().body("Amount is required.");
        }
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body("Balance cannot be negative.");
        }
        try {
            User updatedUser = userService.updateAccountBalance(id, newBalance);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
}
