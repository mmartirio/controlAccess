package com.example.controlAccess.config;

import com.example.controlAccess.models.EmployeeModel;
import com.example.controlAccess.repositories.EmployeeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final EmployeeRepository employeeRepository;
    private final CorsConfig corsConfig; // Injeção de dependência do CorsConfig

    public SecurityConfig(EmployeeRepository employeeRepository, CorsConfig corsConfig) {
        this.employeeRepository = employeeRepository;
        this.corsConfig = corsConfig; // Configuração CORS sendo injetada
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Desabilita CSRF para APIs sem sessão
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/login", "/api/employees/**", "/api/visitors/**", "/api/visits/**").permitAll()  // Permite acesso sem autenticação
                        .requestMatchers("/admin").hasAuthority("ADMIN")  // Restringe para usuários com role ADMIN
                        .requestMatchers("/user").authenticated()  // Qualquer usuário autenticado pode acessar
                        .anyRequest().authenticated()  // Outras rotas exigem autenticação
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Sem sessão
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()));  // Usando a configuração CORS

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            EmployeeModel employee = employeeRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

            return User.withUsername(employee.getUsername())
                    .password(employee.getPassword())
                    .authorities("ROLE_" + employee.getRole().name())  // Adiciona prefixo ROLE_ para garantir que a autoridade seja reconhecida
                    .build();
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Cria o codificador de senha BCrypt
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(List.of(authProvider));
    }
}
