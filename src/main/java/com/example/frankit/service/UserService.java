package com.example.frankit.service;

import com.example.frankit.dto.request.user.UserDto;
import com.example.frankit.entity.user.User;
import com.example.frankit.exception.UserException;
import com.example.frankit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.frankit.exception.UserErrorCode.DUPLICATE_USER_ID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(UserDto dto) {
        createUserValidation(dto);
        User user = dto.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void createUserValidation(UserDto dto) {
        if(userRepository.findByUserId(dto.getUserId()).isPresent()) {
            throw new UserException(DUPLICATE_USER_ID, "중복된 아이디 입니다. userId - ".formatted(dto.getUserId()));
        }
    }

}
