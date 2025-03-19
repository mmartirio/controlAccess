package com.example.controlAccess.config;

import com.example.controlAccess.models.EmployeeModel;
import com.example.controlAccess.repositories.EmployeeRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.Optional;

@Component
public class AdminInitializer {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Construtor para injeção de dependências
    public AdminInitializer(EmployeeRepository employeeRepository, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Este método será chamado automaticamente após o contexto ser iniciado
    @PostConstruct
    public void createAdminUser() {
        String adminUsername = "admin"; // username é o campo que representa o email

        // Verifica se o administrador já existe no banco de dados
        Optional<EmployeeModel> existingAdmin = employeeRepository.findByUsername(adminUsername);

        if (existingAdmin.isEmpty()) {
            System.out.println("Criando usuário administrador...");

            EmployeeModel admin = new EmployeeModel();
            admin.setUsername(adminUsername);
            admin.setName("Administrador");
            admin.setSurName("Sistema");
            admin.setRg("000000000");
            admin.setPhone("00000000000");
            admin.setEmail("admin@example.com"); // email associado ao username
            admin.setPassword(passwordEncoder.encode("admin123")); // Senha criptografada
            admin.setRole(EmployeeModel.Role.ROLE_ADMIN); // Enum correto
            admin.setAccountNonExpired(true);
            admin.setAccountNonLocked(true);
            admin.setCredentialsNonExpired(true);
            admin.setEnabled(true);

            employeeRepository.save(admin);
            System.out.println("Usuário administrador criado com sucesso!");
        } else {
            System.out.println("Usuário administrador já existe, nenhuma ação necessária.");
        }
    }
}
