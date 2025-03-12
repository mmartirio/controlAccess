package com.example.controlAccess.controllers;

import com.example.controlAccess.dtos.VisitDTO;
import com.example.controlAccess.models.VisitModel;
import com.example.controlAccess.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    @Autowired
    private VisitService visitService;

    // Criar uma nova visita
    @PostMapping
    public ResponseEntity<VisitDTO> createVisit(@Valid @RequestBody VisitDTO visitDTO) {
        VisitModel savedVisit = visitService.createVisit(visitDTO);
        return new ResponseEntity<>(convertToDTO(savedVisit), HttpStatus.CREATED);
    }

    // Buscar visita por ID
    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> getVisitById(@PathVariable Long id) {
        Optional<VisitModel> visit = visitService.getVisitById(id);
        return visit.map(value -> new ResponseEntity<>(convertToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Buscar todas as visitas
    @GetMapping("/all")
    public ResponseEntity<List<VisitDTO>> getAllVisits() {
        List<VisitDTO> visitDTOs = visitService.getAllVisits().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return visitDTOs.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(visitDTOs, HttpStatus.OK);
    }

    // Buscar visitas por motivo
    @GetMapping("/reason/{visitReason}")
    public ResponseEntity<List<VisitDTO>> getVisitsByReason(@PathVariable String visitReason) {
        return getVisitsResponse(visitService.getVisitsByReason(visitReason));
    }

    // Buscar visitas por setor
    @GetMapping("/sector/{sector}")
    public ResponseEntity<List<VisitDTO>> getVisitsBySector(@PathVariable String sector) {
        return getVisitsResponse(visitService.getVisitsBySector(sector));
    }

    // Buscar visitas por data
    @GetMapping("/date/{visitDate}")
    public ResponseEntity<List<VisitDTO>> getVisitsByDate(@PathVariable String visitDate) {
        LocalDate parsedDate = LocalDate.parse(visitDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return getVisitsResponse(visitService.getVisitsByDate(parsedDate));
    }

    // Buscar visitas por visitorId
    @GetMapping("/visitor/{visitorId}")
    public ResponseEntity<List<VisitDTO>> getVisitsByVisitorId(@PathVariable Long visitorId) {
        List<VisitModel> visits = visitService.getVisitsByVisitorId(visitorId);
        return getVisitsResponse(visits);
    }

    // Atualizar visita
    @PutMapping("/{id}")
    public ResponseEntity<VisitDTO> updateVisit(@PathVariable Long id, @Valid @RequestBody VisitDTO visitDTO) {
        Optional<VisitModel> updatedVisit = visitService.updateVisit(id, visitDTO);
        return updatedVisit.map(value -> new ResponseEntity<>(convertToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Excluir visita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        return visitService.deleteVisit(id) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Método auxiliar para converter VisitModel para VisitDTO
    private VisitDTO convertToDTO(VisitModel visit) {
        return new VisitDTO(
                visit.getId(),
                visit.getVisitReason(),
                visit.getSector(),
                visit.getVisitDate(),
                visit.getVisitor().getId(), // ID do visitante
                visit.getVisitor().getName(), // Nome do visitante
                visit.getVisitor().getSurName() // Sobrenome do visitante
        );
    }

    // Método auxiliar para evitar repetição na resposta das listas de visitas
    private ResponseEntity<List<VisitDTO>> getVisitsResponse(List<VisitModel> visits) {
        List<VisitDTO> visitDTOs = visits.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return visitDTOs.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(visitDTOs, HttpStatus.OK);
    }
}
