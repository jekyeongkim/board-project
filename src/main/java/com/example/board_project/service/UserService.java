package com.example.board_project.service;

import com.example.board_project.entity.SiteUser;
import com.example.board_project.repository.UserRepository;
import com.example.board_project.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public SiteUser create(String username, String password, String email) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            throw new DataNotFoundException("siteuser not found");
        });
    }
}
