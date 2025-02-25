package com.example.frankit.dto.security;

import com.example.frankit.dto.request.user.UserDto;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@ToString
public class CustomUserDetails implements UserDetails {
    Long id;
    String userId;
    String password;
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String userId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.authorities = authorities;
    }

    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override public boolean isAccountNonExpired() {return true;}
    @Override public boolean isAccountNonLocked() {return true;}
    @Override public boolean isCredentialsNonExpired() {return true;}
    @Override public boolean isEnabled() {return true;}

    public UserDto toDto() {
        return UserDto.of(id, userId, password);
    }
}
