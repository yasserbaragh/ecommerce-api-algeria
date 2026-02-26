package com.example.demo.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserTable Register(UserTable user) {
        return userRepository.save(user);
    }

    @Transactional
    public void signup(RegisterRecord request) {
        String email = request.email();
        Optional<UserTable> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            //throw new DuplicateException(String.format("User with the email address '%s' already exists.", email));
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        UserTable user = new UserTable(request.name(), email, hashedPassword);
        userRepository.save(user);
    }




}
