package com.example.frankit.dto.request.user;

import com.example.frankit.entity.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank @Email private String userId;
    @NotBlank private String password;

    public User toEntity() {
        return User.of(userId, password);
    }

    public UserDto toDto() {
        return UserDto.of(userId, password);
    }
}
