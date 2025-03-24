package com.example.controlAccess.repositories;

import com.example.controlAccess.models.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeModel, Long> {

    // Native Query para buscar funcionário pelo username, retornando um Optional
    @Query(value = "SELECT * FROM employee_model WHERE username = ?1", nativeQuery = true)
    Optional<EmployeeModel> findByUsername(String Username);

    // Native Query para buscar funcionário pelo nome
    @Query(value = "SELECT * FROM employee_model WHERE name = ?1", nativeQuery = true)
    List<EmployeeModel> findByName(String name);

    // Native Query para buscar funcionário pelo sobrenome
    @Query(value = "SELECT * FROM employee_model WHERE surName = ?1", nativeQuery = true)
    List<EmployeeModel> findBySurName(String surName);

    // Native Query para buscar funcionário pelo email, retornando um Optional
    @Query(value = "SELECT * FROM employee_model WHERE email = ?1", nativeQuery = true)
    Optional<EmployeeModel> findByEmail(String email);

    // Native Query para buscar todos os funcionários
    @Query(value = "SELECT * FROM employee_model", nativeQuery = true)
    List<EmployeeModel> findAllEmployees();

    // Busca funcionários por role (ADMIN ou USER)
    @Query(value = "SELECT * FROM employee_model WHERE role = ?1", nativeQuery = true)
    List<EmployeeModel> findByRole(EmployeeModel.Role role);

    // Busca funcionários ativos (enabled = true)
    @Query(value = "SELECT * FROM employee_model WHERE enabled = ?1", nativeQuery = true)
    List<EmployeeModel> findByEnabledTrue();

    // Busca funcionários cuja conta não está expirada
    @Query(value = "SELECT * FROM employee_model WHERE Account_non_Expired = ?1", nativeQuery = true)
    List<EmployeeModel> findByAccountNonExpiredTrue();

    // Busca funcionários cuja conta não está bloqueada
    @Query(value = "SELECT * FROM employee_model WHERE account_non_locked = ?1", nativeQuery = true)
    List<EmployeeModel> findByAccountNonLockedTrue();

    // Busca funcionários cujas credenciais não estão expiradas
    @Query(value = "SELECT * FROM employee_model WHERE credentials_non_expired = ?1", nativeQuery = true)
    List<EmployeeModel> findByCredentialsNonExpiredTrue();
}

