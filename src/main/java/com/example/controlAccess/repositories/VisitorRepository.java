package com.example.controlAccess.repositories;

import com.example.controlAccess.models.VisitorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<VisitorModel, Long> {

    // Native Query para buscar visitante pelo nome
    @Query(value = "SELECT * FROM visitor_model WHERE name = ?1", nativeQuery = true)
    List<VisitorModel> findByName(String name);

    // Native Query para buscar visitante pelo sobrenome
    @Query(value = "SELECT * FROM visitor_model WHERE surname = ?1", nativeQuery = true)
    List<VisitorModel> findBySurname(String surname);

    // Native Query para buscar visitante pelo RG (pode ser útil se for um identificador único)
    @Query(value = "SELECT * FROM visitor_model WHERE rg = ?1", nativeQuery = true)
    VisitorModel findByRg(String rg);

    // Native Query para buscar visitante pela URL da foto
    @Query(value = "SELECT * FROM visitor_model WHERE photo = ?1", nativeQuery = true)
    List<VisitorModel> findByPhoto(String photo);

    // Native Query para buscar todos os visitantes
    @Query(value = "SELECT * FROM visitor_model", nativeQuery = true)
    List<VisitorModel> findAllVisitors();
}
