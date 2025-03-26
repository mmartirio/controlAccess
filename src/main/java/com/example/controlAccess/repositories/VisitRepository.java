package com.example.controlAccess.repositories;

import com.example.controlAccess.models.VisitModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<VisitModel, Long> {

    // NativeQuery para buscar visitas por visitor_id
    @Query(value = "SELECT * FROM visits WHERE visitor_id = :visitorId", nativeQuery = true)
    List<VisitModel> findByVisitorId(@Param("visitorId") Long visitorId);

    // NativeQuery para buscar visitas por nome do responsável (case insensitive)
    @Query(value = "SELECT * FROM visits WHERE LOWER(responsible_name) LIKE LOWER(CONCAT('%', :responsibleName, '%'))",
            nativeQuery = true)
    List<VisitModel> findByResponsibleNameContainingIgnoreCase(@Param("responsibleName") String responsibleName);

    // NativeQuery para busca por motivo da visita (case insensitive)
    @Query(value = "SELECT * FROM visits WHERE LOWER(visit_reason) LIKE LOWER(CONCAT('%', :visitReason, '%'))",
            nativeQuery = true)
    List<VisitModel> findByVisitReasonContainingIgnoreCase(@Param("visitReason") String visitReason);

    // NativeQuery para busca por setor (case insensitive)
    @Query(value = "SELECT * FROM visits WHERE LOWER(sector) LIKE LOWER(CONCAT('%', :sector, '%'))",
            nativeQuery = true)
    List<VisitModel> findBySectorContainingIgnoreCase(@Param("sector") String sector);

    // NativeQuery para buscar visitas por data exata
    @Query(value = "SELECT * FROM visits WHERE CAST(visit_date AS DATE) = CAST(:visitDate AS DATE)",
            nativeQuery = true)
    List<VisitModel> findByVisitDate(@Param("visitDate") ZonedDateTime visitDate);

    // NativeQuery para buscar visitas entre duas datas (inclusive)
    @Query(value = "SELECT * FROM visits WHERE visit_date BETWEEN :start AND :end",
            nativeQuery = true)
    List<VisitModel> findByVisitDateBetween(@Param("start") ZonedDateTime start,
                                            @Param("end") ZonedDateTime end);

    // NativeQuery para buscar visitas após uma data específica
    @Query(value = "SELECT * FROM visits WHERE visit_date >= :start",
            nativeQuery = true)
    List<VisitModel> findByVisitDateAfter(@Param("start") ZonedDateTime start);

    // NativeQuery para buscar visitas antes de uma data específica
    @Query(value = "SELECT * FROM visits WHERE visit_date <= :end",
            nativeQuery = true)
    List<VisitModel> findByVisitDateBefore(@Param("end") ZonedDateTime end);
}