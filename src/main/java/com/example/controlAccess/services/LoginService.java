package com.example.controlAccess.services;

import com.example.controlAccess.models.EmployeeModel;
import com.example.controlAccess.repositories.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateUser(String username, String password) {
        return employeeRepository.findByUsername(username)
                .map(employee -> passwordEncoder.matches(password, employee.getPassword()))
                .orElse(false);
    }
}
