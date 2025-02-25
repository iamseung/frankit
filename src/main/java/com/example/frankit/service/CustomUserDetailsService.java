package com.example.frankit.service;

import com.example.frankit.entity.user.User;
import com.example.frankit.dto.security.CustomUserDetails;
import com.example.frankit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 사용자 인증 처리
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다. - userId : " + userId));

        return new CustomUserDetails(
                user.getId(),
                user.getUserId(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}