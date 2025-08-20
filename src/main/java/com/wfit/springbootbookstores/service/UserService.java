package com.wfit.springbootbookstores.service;

import com.wfit.springbootbookstores.entity.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> loginUser(String username, String password, User.UserType userType);
    Optional<User> getUserById(Integer id);
    User updateUser(User user);
    void deleteUser(Integer id);
    List<User> getAllUsers();
    List<User> getUsersByUserType(User.UserType userType);
    // 设置用户账户余额为指定金额（amount为新的余额值，不是增量）
    User updateAccountBalance(Integer userId, BigDecimal amount);
    boolean existsByUsername(String username);
}
