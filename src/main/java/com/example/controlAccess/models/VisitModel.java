package com.example.controlAccess.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.ZonedDateTime;


@Entity
public class VisitModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String visitReason;
    private String sector;
    private ZonedDateTime visitDate = ZonedDateTime.now();
    private String responsibleName; // Novo campo para o nome do responsável

    // Associação Many-to-One com VisitorModel (mantida)
    @ManyToOne
    @JoinColumn(name = "visitor_id", referencedColumnName = "id")
    private VisitorModel visitor;


    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public ZonedDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(ZonedDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public VisitorModel getVisitor() {
        return visitor;
    }

    public void setVisitor(VisitorModel visitor) {
        this.visitor = visitor;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }

    // Métodos auxiliares para obter id, nome e sobrenome do visitante
    public Long getVisitorId() {
        return visitor != null ? visitor.getId() : null;
    }

    public String getVisitorName() {
        return visitor != null ? visitor.getName() : null;
    }

    public String getVisitorSurName() {
        return visitor != null ? visitor.getSurName() : null;
    }
}