package com.example.controlAccess.controllers;

import com.example.controlAccess.dtos.VisitorDTO;
import com.example.controlAccess.models.VisitorModel;
import com.example.controlAccess.services.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    // Endpoint para criar um visitante
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<VisitorDTO> createVisitor(@Valid @RequestBody VisitorDTO visitorDTO) {
        // Verificação da foto
        if (visitorDTO.photo() == null || visitorDTO.photo().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Foto obrigatória
        }

        try {
            // Criando e salvando o visitante
            VisitorModel visitorModel = new VisitorModel();
            visitorModel.setName(visitorDTO.name());
            visitorModel.setSurName(visitorDTO.surName());
            visitorModel.setRg(visitorDTO.rg());
            visitorModel.setPhone(visitorDTO.phone());
            visitorModel.setPhoto(visitorDTO.photo()); // Foto já está em Base64

            VisitorModel savedVisitor = visitorService.createVisitor(visitorModel);

            // Convertendo para DTO de resposta
            VisitorDTO savedVisitorDTO = new VisitorDTO(
                    savedVisitor.getId(),
                    savedVisitor.getName(),
                    savedVisitor.getSurName(),
                    savedVisitor.getRg(),
                    savedVisitor.getPhone(),
                    savedVisitor.getPhoto()
            );

            return new ResponseEntity<>(savedVisitorDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            // Retorna uma resposta de erro genérico
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para buscar um visitante pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<VisitorDTO> getVisitorById(@PathVariable Long id) {
        Optional<VisitorModel> visitor = visitorService.getVisitorById(id);

        // Retorna o visitante encontrado ou uma resposta de erro
        return visitor.map(value -> new ResponseEntity<>(new VisitorDTO(
                value.getId(),
                value.getName(),
                value.getSurName(),
                value.getRg(),
                value.getPhone(),
                value.getPhoto() // Foto já está em Base64
        ), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para buscar todos os visitantes
    @GetMapping("/all")
    public ResponseEntity<List<VisitorDTO>> getAllVisitors() {
        List<VisitorModel> visitors = visitorService.getAllVisitors();
        if (visitors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Mapeando os visitantes para o formato de DTO
        List<VisitorDTO> visitorDTOs = visitors.stream().map(visitor -> new VisitorDTO(
                visitor.getId(),
                visitor.getName(),
                visitor.getSurName(),
                visitor.getRg(),
                visitor.getPhone(),
                visitor.getPhoto()
        )).collect(Collectors.toList());

        return new ResponseEntity<>(visitorDTOs, HttpStatus.OK);
    }

    // Endpoint para atualizar um visitante
    @PutMapping("/{id}")
    public ResponseEntity<VisitorDTO> updateVisitor(@PathVariable Long id, @Valid @RequestBody VisitorDTO visitorDTO) {
        // Verificação da foto
        if (visitorDTO.photo() == null || visitorDTO.photo().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Foto obrigatória
        }

        try {
            VisitorModel visitorModel = new VisitorModel();
            visitorModel.setId(id);
            visitorModel.setName(visitorDTO.name());
            visitorModel.setSurName(visitorDTO.surName());
            visitorModel.setRg(visitorDTO.rg());
            visitorModel.setPhone(visitorDTO.phone());
            visitorModel.setPhoto(visitorDTO.photo()); // Foto já está em Base64

            Optional<VisitorModel> updatedVisitor = visitorService.updateVisitor(id, visitorModel);

            // Retorna o visitante atualizado ou um erro caso não seja encontrado
            return updatedVisitor.map(value -> new ResponseEntity<>(new VisitorDTO(
                    value.getId(),
                    value.getName(),
                    value.getSurName(),
                    value.getRg(),
                    value.getPhone(),
                    value.getPhoto()
            ), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            // Retorna uma resposta de erro genérico
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para excluir um visitante
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisitor(@PathVariable Long id) {
        boolean isDeleted = visitorService.deleteVisitor(id);

        // Retorna sucesso ou erro caso não encontrado
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
