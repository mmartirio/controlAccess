package com.example.controlAccess.services;

import com.example.controlAccess.models.VisitorModel;
import com.example.controlAccess.repositories.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    // Método para criar visitante
    public VisitorModel createVisitor(VisitorModel visitorModel) {
        // Aqui você pode verificar se a foto é uma string (base64), já que o dado está em base64
        if (visitorModel.getPhoto() instanceof String) {
            // A foto está em base64, então pode ser armazenada assim
        }

        // Salva o visitante no banco
        return visitorRepository.save(visitorModel);
    }

    // Método para obter um visitante por ID
    public Optional<VisitorModel> getVisitorById(Long id) {
        return visitorRepository.findById(id);
    }

    // Método para obter todos os visitantes
    public List<VisitorModel> getAllVisitors() {
        return visitorRepository.findAll();
    }

    // Método para atualizar um visitante
    public Optional<VisitorModel> updateVisitor(Long id, VisitorModel visitorModel) {
        if (visitorRepository.existsById(id)) {
            // Aqui a foto pode estar em base64, portanto, não precisa de verificação do tipo byte[]
            visitorModel.setId(id);  // Garantir que o ID seja atribuído ao objeto de modelo
            return Optional.of(visitorRepository.save(visitorModel));
        }
        return Optional.empty();
    }

    // Método para excluir visitante
    public boolean deleteVisitor(Long id) {
        if (visitorRepository.existsById(id)) {
            visitorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
