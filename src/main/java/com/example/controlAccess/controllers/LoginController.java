package com.example.controlAccess.controllers;

import com.example.controlAccess.dtos.EmployeeDTO;
import com.example.controlAccess.models.EmployeeModel;
import com.example.controlAccess.services.EmployeeService;
import com.example.controlAccess.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") // Permite requisições do frontend
public class LoginController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;

    public LoginController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, EmployeeService employeeService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> processLogin(@RequestBody EmployeeDTO employeeDTO) {
        try {
            // Autenticar o usuário
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(employeeDTO.username(), employeeDTO.password())
            );

            // Gerar o token
            String token = jwtUtil.generateToken(employeeDTO.username());

            // Obter o usuário completo (que inclui a role)
            EmployeeModel employee = employeeService.findByUsername(employeeDTO.username());

            if (employee != null) {
                String role = String.valueOf(employee.getRole()); // Convertendo a Role para String
                // Incluir a role no retorno da resposta
                return ResponseEntity.ok(Map.of(
                        "message", "Login bem-sucedido!",
                        "token", token,
                        "role", role // Retorna a role convertida para String
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Usuário não encontrado"));
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuário ou senha inválidos!"));
        }
    }

    // Endpoint para buscar a role do usuário
    @GetMapping("/employee/{username}")
    public ResponseEntity<Map<String, String>> getEmployeeRole(@PathVariable String username) {
        EmployeeModel employee = employeeService.findByUsername(username);

        if (employee != null) {
            // Convertendo a role para String antes de retornar
            String role = String.valueOf(employee.getRole());
            return ResponseEntity.ok(Map.of("role", role)); // Retorna a role como String
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Usuário não encontrado"));
        }
    }
}
