package com.example.controlAccess.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VisitorDTO(
        Long id,

        @NotBlank(message = "Nome não pode ser vazio")
        String name,

        @NotBlank(message = "Sobrenome não pode ser vazio")
        String surName,

        @NotBlank(message = "RG não pode ser vazio")
        String rg,

        @NotBlank(message = "Telefone não pode ser vazio")
        String phone,

        @NotBlank(message = "A URL da foto não pode ser vazia")
        String photo
) {}
