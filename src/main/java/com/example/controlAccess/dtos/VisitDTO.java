package com.example.controlAccess.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;


public record VisitDTO(
        Long id,

        @NotBlank(message = "Motivo da visita é obrigatório")
        @Size(min = 2, max = 100, message = "Motivo deve ter entre 2 e 100 caracteres")
        String visitReason,

        @NotBlank(message = "Setor é obrigatório")
        @Size(min = 2, max = 50, message = "Setor deve ter entre 2 e 50 caracteres")
        String sector,

        @NotNull(message = "Data da visita é obrigatória")
        @FutureOrPresent(message = "Data da visita deve ser atual ou futura")
        ZonedDateTime visitDate,

        // Informações mínimas necessárias do visitante
        @NotNull(message = "ID do visitante é obrigatório")
        Long visitorId,

        // Informações mínimas necessárias do funcionário
        @NotBlank(message = "Nome do responsável é obrigatório")
        @Size(min = 2, max = 100, message = "Nome do responsável deve ter entre 2 e 100 caracteres")
        String responsibleName
) {
}