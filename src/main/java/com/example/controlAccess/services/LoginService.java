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

    // Método para validar o login do usuário
    public boolean validateUser(String username, String password) {
        // Buscar o usuário pelo username
        Optional<EmployeeModel> employeeOptional = employeeRepository.findByUsername(username);

        // Verificar se o usuário existe
        if (employeeOptional.isEmpty()) {
            // Adicionando log para quando o usuário não for encontrado
            System.out.println("Usuário não encontrado: " + username);
            return false;
        }

        EmployeeModel employee = employeeOptional.get();

        // Verificar se a senha fornecida corresponde à senha codificada no banco de dados
        boolean passwordMatches = passwordEncoder.matches(password, employee.getPassword());

        // Log para verificar se a senha não corresponde
        if (!passwordMatches) {
            System.out.println("Senha inválida para o usuário: " + username);
        }

        // Retorna se a senha é válida ou não
        return passwordMatches;
    }
}
