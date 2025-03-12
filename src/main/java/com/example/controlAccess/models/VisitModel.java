package com.example.controlAccess.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class VisitModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String visitReason;
    private String sector;
    private LocalDate visitDate = LocalDate.now();

    // Associação Many-to-One com VisitorModel
    @ManyToOne
    @JoinColumn(name = "visitor_id", referencedColumnName = "id")
    private VisitorModel visitor;

    // Associação Many-to-One com EmployeeModel
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private EmployeeModel employee;

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

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public VisitorModel getVisitor() {
        return visitor;
    }

    public void setVisitor(VisitorModel visitor) {
        this.visitor = visitor;
    }

    public EmployeeModel getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeModel employee) {
        this.employee = employee;
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
