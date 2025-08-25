package com.wfit.springbootbookstores.service.impl;

import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.repository.UserRepository;
import com.wfit.springbootbookstores.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // You might want to inject a password encoder here, e.g., BCryptPasswordEncoder
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        // Before saving, encode the password
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        // 设置注册时间为当前时间
        user.setRegistrationTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> loginUser(String username, String password, User.UserType userType) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Implement password matching logic here
            // return user.getUserType() == userType && passwordEncoder.matches(password, user.getPassword()) ? userOptional : Optional.empty();
            return user.getUserType() == userType && user.getPassword().equals(password) ? userOptional : Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(User user) {
        // Consider handling password updates separately or re-encoding if changed
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByUserType(User.UserType userType) {
        return userRepository.findByUserType(userType);
    }

    @Override
    public User updateAccountBalance(Integer userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Ensure the user is an OWNER before updating balance
        if (user.getUserType() != User.UserType.OWNER) {
            throw new RuntimeException("Only OWNER type users have account balance.");
        }
        // 直接设置新余额（前端已经计算好了）
        user.setAccountBalance(amount);
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
