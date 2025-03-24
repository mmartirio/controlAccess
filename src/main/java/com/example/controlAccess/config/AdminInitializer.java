package com.example.controlAccess.config;

import com.example.controlAccess.models.EmployeeModel;
import com.example.controlAccess.repositories.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.Optional;

@Component
public class AdminInitializer {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(EmployeeRepository employeeRepository,
                            PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createAdminUser() {
        String adminUsername = "admin";

        Optional<EmployeeModel> existingAdmin = employeeRepository.findByUsername(adminUsername);

        if (existingAdmin.isEmpty()) {
            System.out.println("Criando usuário administrador...");

            EmployeeModel admin = new EmployeeModel();
            admin.setUsername(adminUsername);
            admin.setName("Administrador");
            admin.setSurName("Sistema");
            admin.setRg("000000000");
            admin.setPhone("00000000000");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(EmployeeModel.Role.ROLE_ADMIN);
            admin.setAccountNonExpired(true);
            admin.setAccountNonLocked(true);
            admin.setCredentialsNonExpired(true);
            admin.setEnabled(true);

            employeeRepository.save(admin);
            System.out.println("Usuário administrador criado com sucesso!");
        } else {
            System.out.println("Usuário administrador já existe.");
        }
    }
}