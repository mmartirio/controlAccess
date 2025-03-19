package com.example.controlAccess.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
public class EmployeeModel extends UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Isso vai gerar o ID automaticamente para UserModel e suas subclasses
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean accountNonExpired = false;
    private boolean accountNonLocked = false;
    private boolean credentialsNonExpired = false;
    private boolean enabled = true;

    public EmployeeModel() {
        super();
    }

    public EmployeeModel(Long id, String username, String name, String surName, String rg, String phone, String email, String password, Role role,
                         boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        super(id, name, surName, rg, phone);
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public enum Role {
        ROLE_ADMIN,
        ROLE_USER
    }
}
