package com.example.controlAccess.controllers;

import com.example.controlAccess.dtos.VisitorDTO;
import com.example.controlAccess.models.VisitorModel;
import com.example.controlAccess.services.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    // Endpoint para criar um visitante
    @PostMapping
    public ResponseEntity<VisitorDTO> createVisitor(@Valid @RequestBody VisitorDTO visitorDTO) {
        try {
            // Foto já está no formato Base64 no DTO, não precisa converter mais
            String photoBase64 = visitorDTO.photo();  // Assumindo que o campo "photo" no DTO já é uma string base64

            // Convertendo o DTO para VisitorModel
            VisitorModel visitorModel = new VisitorModel();
            visitorModel.setName(visitorDTO.name());
            visitorModel.setSurName(visitorDTO.surName());
            visitorModel.setRg(visitorDTO.rg());
            visitorModel.setPhone(visitorDTO.phone());
            visitorModel.setPhoto(photoBase64);  // Definindo a foto diretamente

            // Chamando o serviço para salvar o visitante
            VisitorModel savedVisitor = visitorService.createVisitor(visitorModel);

            // Convertendo o modelo salvo para DTO
            VisitorDTO savedVisitorDTO = new VisitorDTO(
                    savedVisitor.getId(),
                    savedVisitor.getName(),
                    savedVisitor.getSurName(),
                    savedVisitor.getRg(),
                    savedVisitor.getPhone(),
                    savedVisitor.getPhoto() // Foto como base64
            );

            return new ResponseEntity<>(savedVisitorDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para buscar um visitante pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<VisitorDTO> getVisitorById(@PathVariable Long id) {
        Optional<VisitorModel> visitor = visitorService.getVisitorById(id);
        return visitor.map(value -> {
            // Convertendo o modelo para DTO
            VisitorDTO visitorDTO = new VisitorDTO(
                    value.getId(),
                    value.getName(),
                    value.getSurName(),
                    value.getRg(),
                    value.getPhone(),
                    value.getPhoto() // Foto como base64
            );
            return new ResponseEntity<>(visitorDTO, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para buscar todos os visitantes
    @GetMapping("/all")
    public ResponseEntity<List<VisitorDTO>> getAllVisitors() {
        List<VisitorModel> visitors = visitorService.getAllVisitors();
        if (visitors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convertendo os modelos para DTOs
        List<VisitorDTO> visitorDTOs = visitors.stream().map(visitor -> {
            return new VisitorDTO(
                    visitor.getId(),
                    visitor.getName(),
                    visitor.getSurName(),
                    visitor.getRg(),
                    visitor.getPhone(),
                    visitor.getPhoto() // Foto como base64
            );
        }).collect(Collectors.toList());

        return new ResponseEntity<>(visitorDTOs, HttpStatus.OK);
    }

    // Endpoint para atualizar um visitante
    @PutMapping("/{id}")
    public ResponseEntity<VisitorDTO> updateVisitor(@PathVariable Long id, @Valid @RequestBody VisitorDTO visitorDTO) {
        try {
            // Foto já está no formato Base64 no DTO, não precisa converter mais
            String photoBase64 = visitorDTO.photo();  // Assumindo que o campo "photo" no DTO já é uma string base64

            // Convertendo o DTO para VisitorModel
            VisitorModel visitorModel = new VisitorModel();
            visitorModel.setId(id);
            visitorModel.setName(visitorDTO.name());
            visitorModel.setSurName(visitorDTO.surName());
            visitorModel.setRg(visitorDTO.rg());
            visitorModel.setPhone(visitorDTO.phone());
            visitorModel.setPhoto(photoBase64);  // Definindo a foto diretamente

            Optional<VisitorModel> updatedVisitor = visitorService.updateVisitor(id, visitorModel);

            return updatedVisitor.map(value -> {
                // Convertendo o modelo atualizado para DTO
                VisitorDTO visitorDTOResponse = new VisitorDTO(
                        value.getId(),
                        value.getName(),
                        value.getSurName(),
                        value.getRg(),
                        value.getPhone(),
                        value.getPhoto() // Foto como base64
                );
                return new ResponseEntity<>(visitorDTOResponse, HttpStatus.OK);
            }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para excluir um visitante
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisitor(@PathVariable Long id) {
        boolean isDeleted = visitorService.deleteVisitor(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
