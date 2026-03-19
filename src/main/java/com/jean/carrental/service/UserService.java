package com.jean.carrental.service;

import com.jean.carrental.dto.UserDTO;
import com.jean.carrental.model.User;
import com.jean.carrental.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password!");
        }
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                null
        );
    }

    public UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                null
        );
    }
}
