package com.example.controlAccess.services;

import com.example.controlAccess.dtos.VisitDTO;
import com.example.controlAccess.models.VisitModel;
import com.example.controlAccess.models.VisitorModel;
import com.example.controlAccess.repositories.VisitRepository;
import com.example.controlAccess.repositories.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    // Criar uma nova visita
    public VisitModel createVisit(VisitDTO visitDTO) {
        VisitModel visit = new VisitModel();
        visit.setVisitReason(visitDTO.visitReason());
        visit.setSector(visitDTO.sector());
        visit.setVisitDate(visitDTO.visitDate());

        // Buscar visitante pelo ID
        Optional<VisitorModel> visitor = visitorRepository.findById(visitDTO.visitorId());
        visitor.ifPresent(visit::setVisitor);

        return visitRepository.save(visit);
    }

    // Buscar visita por ID
    public Optional<VisitModel> getVisitById(Long id) {
        return visitRepository.findById(id);
    }

    // Buscar todas as visitas
    public List<VisitModel> getAllVisits() {
        return visitRepository.findAll();
    }

    // Buscar visitas por motivo
    public List<VisitModel> getVisitsByReason(String visitReason) {
        return visitRepository.findByVisitReason(visitReason);
    }

    // Buscar visitas por setor
    public List<VisitModel> getVisitsBySector(String sector) {
        return visitRepository.findBySector(sector);
    }

    // Buscar visitas por data
    public List<VisitModel> getVisitsByDate(LocalDate visitDate) {
        return visitRepository.findByVisitDate(visitDate);
    }

    // Buscar visitas por visitante
    public List<VisitModel> getVisitsByVisitorId(Long visitorId) {
        return visitRepository.findByVisitorId(visitorId);
    }

    // Atualizar visita
    public Optional<VisitModel> updateVisit(Long id, VisitDTO visitDTO) {
        Optional<VisitModel> existingVisit = visitRepository.findById(id);
        if (existingVisit.isPresent()) {
            VisitModel visit = existingVisit.get();
            visit.setVisitReason(visitDTO.visitReason());
            visit.setSector(visitDTO.sector());
            visit.setVisitDate(visitDTO.visitDate());

            // Atualizar visitante se fornecido
            Optional<VisitorModel> visitor = visitorRepository.findById(visitDTO.visitorId());
            visitor.ifPresent(visit::setVisitor);

            return Optional.of(visitRepository.save(visit));
        }
        return Optional.empty();
    }

    // Excluir visita
    public boolean deleteVisit(Long id) {
        if (visitRepository.existsById(id)) {
            visitRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
