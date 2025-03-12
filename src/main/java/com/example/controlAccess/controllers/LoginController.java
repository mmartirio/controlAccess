package com.example.controlAccess.controllers;

import com.example.controlAccess.dtos.EmployeeDTO;
import com.example.controlAccess.services.LoginService;
import com.example.controlAccess.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") // Permite requisições do frontend
public class LoginController {

    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public LoginController(LoginService loginService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.loginService = loginService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> processLogin(@RequestBody EmployeeDTO employeeDTO) {
        try {
            // Verifica credenciais com o AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(employeeDTO.username(), employeeDTO.password())
            );

            // Gera o token JWT para o usuário autenticado
            String token = jwtUtil.generateToken(employeeDTO.username());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login bem-sucedido!");
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Responde com erro de autenticação
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuário ou senha inválidos!");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
