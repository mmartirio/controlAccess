package com.example.controlAccess.repositories;

import com.example.controlAccess.models.VisitModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<VisitModel, Long> {

    @Query(value = "SELECT * FROM visits WHERE visitor_id = :visitorId", nativeQuery = true)
    List<VisitModel> findByVisitorId(@Param("visitorId") Long visitorId);

    // Native Query para buscar visitas por motivo da visita
    @Query(value = "SELECT * FROM visit_model WHERE visit_reason = :visitReason", nativeQuery = true)
    List<VisitModel> findByVisitReason(@Param("visitReason") String visitReason);

    // Native Query para buscar visitas por setor
    @Query(value = "SELECT * FROM visit_model WHERE sector = :sector", nativeQuery = true)
    List<VisitModel> findBySector(@Param("sector") String sector);

    // Native Query para buscar visitas por data (visitDate)
    @Query(value = "SELECT * FROM visit_model WHERE visit_date = :visitDate", nativeQuery = true)
    List<VisitModel> findByVisitDate(@Param("visitDate") LocalDate visitDate);

    // Native Query para buscar todas as visitas
    @Query(value = "SELECT * FROM visit_model", nativeQuery = true)
    List<VisitModel> findAllVisits();
}
