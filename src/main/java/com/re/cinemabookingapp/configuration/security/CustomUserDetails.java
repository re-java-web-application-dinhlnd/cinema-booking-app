package com.re.cinemabookingapp.configuration.security;

import com.re.cinemabookingapp.entity.User;
import com.re.cinemabookingapp.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() { return user.getPassword(); }

    @Override
    public String getUsername() { return user.getUsername(); }


    @Override
    public boolean isAccountNonLocked() { return user.getStatus() == UserStatus.ACTIVE; }

    @Override
    public boolean isEnabled() { return user.getStatus() == UserStatus.ACTIVE; }
}
