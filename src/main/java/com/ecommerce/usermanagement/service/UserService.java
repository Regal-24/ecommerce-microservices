package com.ecommerce.usermanagement.service;

import com.ecommerce.usermanagement.model.User;
import com.ecommerce.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1.1 Registration
    public User registerUser(User user) {
        // In a real app:
        // - Hash password before saving: user.setPassword(passwordEncoder.encode(user.getPassword()));
        // - Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered!");
        }
        return userRepository.save(user);
    }

    // 1.2 Login (Simplified for now, will need proper security later)
    public Optional<User> loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // In a real app: compare hashed passwords
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty(); // Login failed
    }

    // 1.3 Profile Management - View
    public Optional<User> getUserProfile(Long userId) {
        return userRepository.findById(userId);
    }

    // 1.3 Profile Management - Update
    public User updateProfile(Long userId, User updatedUser) {
        return userRepository.findById(userId).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail()); // Be careful with email updates and uniqueness
            // In a real app, password update would be separate
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found for update: " + userId));
    }

    // You can add more methods for Password Reset later.
}