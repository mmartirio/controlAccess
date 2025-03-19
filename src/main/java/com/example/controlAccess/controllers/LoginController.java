package com.example.controlAccess.controllers;

import com.example.controlAccess.dtos.EmployeeDTO;
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

    public LoginController(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> processLogin(@RequestBody EmployeeDTO employeeDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(employeeDTO.username(), employeeDTO.password())
            );

            String token = jwtUtil.generateToken(employeeDTO.username());

            return ResponseEntity.ok(Map.of(
                    "message", "Login bem-sucedido!",
                    "token", token
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuário ou senha inválidos!"));
        }
    }
}
