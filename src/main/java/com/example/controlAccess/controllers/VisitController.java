package com.example.controlAccess.controllers;

import com.example.controlAccess.dtos.VisitDTO;
import com.example.controlAccess.models.VisitModel;
import com.example.controlAccess.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    public ResponseEntity<VisitDTO> createVisit(@Valid @RequestBody VisitDTO visitDTO) {
        VisitDTO processedVisitDTO = processVisitDateTime(visitDTO);
        VisitModel savedVisit = visitService.createVisit(processedVisitDTO);
        return new ResponseEntity<>(toDTO(savedVisit), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> getVisitById(@PathVariable Long id) {
        Optional<VisitModel> visit = visitService.getVisitById(id);
        return visit.map(value -> new ResponseEntity<>(toDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<VisitDTO>> getAllVisits() {
        List<VisitDTO> visitDTOs = visitService.getAllVisits().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return visitDTOs.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(visitDTOs, HttpStatus.OK);
    }

    @GetMapping("/by-reason/{visitReason}")
    public ResponseEntity<List<VisitDTO>> getVisitsByReason(@PathVariable String visitReason) {
        return buildListResponse(visitService.getVisitsByReason(visitReason));
    }

    @GetMapping("/by-sector/{sector}")
    public ResponseEntity<List<VisitDTO>> getVisitsBySector(@PathVariable String sector) {
        return buildListResponse(visitService.getVisitsBySector(sector));
    }

    @GetMapping("/by-date")
    public ResponseEntity<?> getVisitsByDateRange(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        try {
            ZonedDateTime startDate = start != null ? ZonedDateTime.parse(start) : null;
            ZonedDateTime endDate = end != null ? ZonedDateTime.parse(end) : null;
            return buildListResponse(visitService.getVisitsByDateRange(startDate, endDate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Formato de data inválido. Use o formato ISO-8601 (ex: 2023-10-05T10:15:30+01:00[Europe/Paris])");
        }
    }

    @GetMapping("/by-visitor/{visitorId}")
    public ResponseEntity<List<VisitDTO>> getVisitsByVisitorId(@PathVariable Long visitorId) {
        return buildListResponse(visitService.getVisitsByVisitorId(visitorId));
    }

    @GetMapping("/by-responsible/{responsibleName}")
    public ResponseEntity<List<VisitDTO>> getVisitsByResponsibleName(@PathVariable String responsibleName) {
        return buildListResponse(visitService.getVisitsByResponsibleName(responsibleName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitDTO> updateVisit(@PathVariable Long id, @Valid @RequestBody VisitDTO visitDTO) {
        VisitDTO processedVisitDTO = processVisitDateTime(visitDTO);
        Optional<VisitModel> updatedVisit = visitService.updateVisit(id, processedVisitDTO);
        return updatedVisit.map(value -> new ResponseEntity<>(toDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        return visitService.deleteVisit(id)
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Métodos auxiliares
    private VisitDTO toDTO(VisitModel visit) {
        return new VisitDTO(
                visit.getId(),
                visit.getVisitReason(),
                visit.getSector(),
                visit.getVisitDate(),
                visit.getVisitor() != null ? visit.getVisitor().getId() : null,
                visit.getResponsibleName()
        );
    }

    private ResponseEntity<List<VisitDTO>> buildListResponse(List<VisitModel> visits) {
        if (visits == null || visits.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(
                visits.stream().map(this::toDTO).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    private VisitDTO processVisitDateTime(VisitDTO visitDTO) {
        if (visitDTO.visitDate() != null) {
            // Garante que a data está no fuso horário do servidor
            ZonedDateTime processedDate = visitDTO.visitDate().withZoneSameInstant(ZoneId.systemDefault());
            return new VisitDTO(
                    visitDTO.id(),
                    visitDTO.visitReason(),
                    visitDTO.sector(),
                    processedDate,
                    visitDTO.visitorId(),
                    visitDTO.responsibleName()
            );
        }
        return visitDTO;
    }
}