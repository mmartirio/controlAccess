package com.example.controlAccess.services;

import com.example.controlAccess.dtos.VisitDTO;
import com.example.controlAccess.models.VisitModel;
import com.example.controlAccess.models.VisitorModel;
import com.example.controlAccess.repositories.VisitRepository;
import com.example.controlAccess.repositories.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitorRepository visitorRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository,
                        VisitorRepository visitorRepository) {
        this.visitRepository = visitRepository;
        this.visitorRepository = visitorRepository;
    }

    @Transactional
    public VisitModel createVisit(VisitDTO visitDTO) {
        VisitorModel visitor = visitorRepository.findById(visitDTO.visitorId())
                .orElseThrow(() -> new IllegalArgumentException("Visitante não encontrado com ID: " + visitDTO.visitorId()));

        VisitModel visit = new VisitModel();
        visit.setVisitReason(visitDTO.visitReason());
        visit.setSector(visitDTO.sector());

        // Define a data atual com o fuso horário do servidor se não for fornecida
        visit.setVisitDate(visitDTO.visitDate() != null ?
                visitDTO.visitDate() :
                ZonedDateTime.now(ZoneId.systemDefault()));

        visit.setVisitor(visitor);
        visit.setResponsibleName(visitDTO.responsibleName());

        return visitRepository.save(visit);
    }

    public Optional<VisitModel> getVisitById(Long id) {
        return visitRepository.findById(id);
    }

    public List<VisitModel> getAllVisits() {
        return visitRepository.findAll();
    }

    public List<VisitModel> getVisitsByReason(String visitReason) {
        return visitRepository.findByVisitReasonContainingIgnoreCase(visitReason);
    }

    public List<VisitModel> getVisitsBySector(String sector) {
        return visitRepository.findBySectorContainingIgnoreCase(sector);
    }

    public List<VisitModel> getVisitsByDateRange(ZonedDateTime start, ZonedDateTime end) {
        if (start != null && end != null) {
            return visitRepository.findByVisitDateBetween(start, end);
        } else if (start != null) {
            return visitRepository.findByVisitDateAfter(start);
        } else if (end != null) {
            return visitRepository.findByVisitDateBefore(end);
        }
        return visitRepository.findAll();
    }

    public List<VisitModel> getVisitsByVisitorId(Long visitorId) {
        return visitRepository.findByVisitorId(visitorId);
    }

    public List<VisitModel> getVisitsByResponsibleName(String responsibleName) {
        return visitRepository.findByResponsibleNameContainingIgnoreCase(responsibleName);
    }

    @Transactional
    public Optional<VisitModel> updateVisit(Long id, VisitDTO visitDTO) {
        return visitRepository.findById(id)
                .map(existingVisit -> {
                    existingVisit.setVisitReason(visitDTO.visitReason());
                    existingVisit.setSector(visitDTO.sector());
                    existingVisit.setResponsibleName(visitDTO.responsibleName());

                    if (visitDTO.visitDate() != null) {
                        existingVisit.setVisitDate(visitDTO.visitDate());
                    }

                    if (!existingVisit.getVisitor().getId().equals(visitDTO.visitorId())) {
                        VisitorModel visitor = visitorRepository.findById(visitDTO.visitorId())
                                .orElseThrow(() -> new IllegalArgumentException("Visitante não encontrado com ID: " + visitDTO.visitorId()));
                        existingVisit.setVisitor(visitor);
                    }

                    return visitRepository.save(existingVisit);
                });
    }

    @Transactional
    public boolean deleteVisit(Long id) {
        if (visitRepository.existsById(id)) {
            visitRepository.deleteById(id);
            return true;
        }
        return false;
    }
}