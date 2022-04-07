package com.example.demo.security.userPrinciple;

import com.example.demo.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {

    private String name;
    private String username;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> roles;
    private String avatar;

    public UserPrinciple( String name, String username, String password,
                          Collection<? extends GrantedAuthority> roles,String avatar) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.avatar=avatar;
    }
   public static UserPrinciple build(User user){   // xây dựng user(dạng UserPrinciple) trên hệ thống
        // gán role= authorities của UserDetails
        List<GrantedAuthority> authorities=user.getRoles().stream().map(role ->
            new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UserPrinciple(
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                authorities,
                user.getAvatar()
        );
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
