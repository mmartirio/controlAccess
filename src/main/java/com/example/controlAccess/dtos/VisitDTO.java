package com.example.controlAccess.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record VisitDTO(
        Long id,

        @NotBlank(message = "Motivo da visita não pode ser vazio")
        @Size(min = 2, max = 100, message = "O motivo da visita deve ter entre 2 e 100 caracteres")
        String visitReason,

        @NotBlank(message = "Setor não pode ser vazio")
        @Size(min = 2, max = 50, message = "O setor deve ter entre 2 e 50 caracteres")
        String sector,

        @NotNull(message = "Data da visita não pode ser nula")
        @FutureOrPresent(message = "A data da visita não pode estar no passado")
        LocalDate visitDate,

        @NotNull(message = "ID do visitante não pode ser nulo")
        Long visitorId,

        @NotBlank(message = "Nome do visitante não pode ser vazio")
        @Size(min = 2, max = 100, message = "O nome do visitante deve ter entre 2 e 100 caracteres")
        String visitorName,

        @NotBlank(message = "Sobrenome do visitante não pode ser vazio")
        @Size(min = 2, max = 100, message = "O sobrenome do visitante deve ter entre 2 e 100 caracteres")
        String visitorSurName
) {}
