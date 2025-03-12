package com.example.controlAccess.services;

import com.example.controlAccess.models.EmployeeModel;
import com.example.controlAccess.repositories.EmployeeRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginService(EmployeeRepository employeeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateUser(String username, String password) {
        // Utiliza o repositório para buscar o funcionário pelo username
        Optional<EmployeeModel> employeeOptional = employeeRepository.findByUsername(username);

        if (employeeOptional.isPresent()) {
            EmployeeModel employee = employeeOptional.get();

            // Verifica se a senha fornecida corresponde à senha armazenada (usando BCrypt)
            return passwordEncoder.matches(password, employee.getPassword());
        }

        return false; // Retorna falso se o usuário não for encontrado
    }
}
