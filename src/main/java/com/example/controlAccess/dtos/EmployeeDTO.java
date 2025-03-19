package com.example.controlAccess.dtos;

import com.example.controlAccess.models.EmployeeModel.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeDTO(
        Long id,

        @NotBlank(message = "Nome não pode ser vazio")
        @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
        String name,

        @NotBlank(message = "Sobrenome não pode ser vazio")
        @Size(min = 2, max = 50, message = "O sobrenome deve ter entre 2 e 50 caracteres")
        String surName,

        @NotBlank(message = "Nome de usuário não pode ser vázio")
        String username,

        @NotBlank(message = "RG não pode ser vázio")
        String rg,


        @NotBlank(message = "Email não pode ser vazio")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Telefone não pode ser vazio")
        @Size(min = 10, max = 15, message = "O telefone deve ter entre 10 e 15 caracteres")
        String phone,

        @NotBlank(message = "Senha não pode ser vazia")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String password,

        @NotNull(message = "O papel do usuário deve ser informado")
        Role role,

        @NotNull(message = "A informação de expiração da conta deve ser informada")
        Boolean accountNonExpired,

        @NotNull(message = "A informação de bloqueio da conta deve ser informada")
        Boolean accountNonLocked,

        @NotNull(message = "A informação de expiração das credenciais deve ser informada")
        Boolean credentialsNonExpired,

        @NotNull(message = "A informação de ativação do usuário deve ser informada")
        Boolean enabled
) {}
