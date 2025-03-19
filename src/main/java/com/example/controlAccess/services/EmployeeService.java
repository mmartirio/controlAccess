package com.example.controlAccess.services;

import com.example.controlAccess.dtos.EmployeeDTO;
import com.example.controlAccess.models.EmployeeModel;
import com.example.controlAccess.repositories.EmployeeRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para converter EmployeeModel para EmployeeDTO
    private EmployeeDTO convertToDTO(EmployeeModel employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getSurName(),
                employee.getUsername(),
                employee.getRg(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getPassword(),
                employee.getRole(),
                employee.isAccountNonExpired(),
                employee.isAccountNonLocked(),
                employee.isCredentialsNonExpired(),
                employee.isEnabled()
        );
    }

    // Método para criar um novo funcionário a partir do DTO
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // Codificando a senha antes de salvar
        String encodedPassword = passwordEncoder.encode(employeeDTO.password());

        // Criar um novo EmployeeModel a partir do DTO
        EmployeeModel employee = new EmployeeModel();
        employee.setName(employeeDTO.name());
        employee.setSurName(employeeDTO.surName());
        employee.setEmail(employeeDTO.email());
        employee.setPhone(employeeDTO.phone());
        employee.setPassword(encodedPassword);  // Senha já codificada
        employee.setRole(employeeDTO.role());
        employee.setAccountNonExpired(employeeDTO.accountNonExpired());
        employee.setAccountNonLocked(employeeDTO.accountNonLocked());
        employee.setCredentialsNonExpired(employeeDTO.credentialsNonExpired());
        employee.setEnabled(employeeDTO.enabled());

        employee = employeeRepository.save(employee);

        return convertToDTO(employee);
    }

    // Método para atualizar um funcionário
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Optional<EmployeeModel> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            EmployeeModel employee = employeeOptional.get();
            employee.setName(employeeDTO.name());
            employee.setSurName(employeeDTO.surName());
            employee.setEmail(employeeDTO.email());
            employee.setPhone(employeeDTO.phone());

            // Atualiza a senha apenas se for fornecida
            if (employeeDTO.password() != null && !employeeDTO.password().isEmpty()) {
                employee.setPassword(passwordEncoder.encode(employeeDTO.password()));  // Codificando a nova senha
            }

            // Atualiza os outros campos
            employee.setRole(employeeDTO.role());
            employee.setAccountNonExpired(employeeDTO.accountNonExpired());
            employee.setAccountNonLocked(employeeDTO.accountNonLocked());
            employee.setCredentialsNonExpired(employeeDTO.credentialsNonExpired());
            employee.setEnabled(employeeDTO.enabled());

            employee = employeeRepository.save(employee);
            return convertToDTO(employee);
        }
        return null;
    }

    // Método para buscar todos os funcionários
    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeModel> employees = employeeRepository.findAll();
        return employees.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Método para buscar funcionário pelo nome
    public List<EmployeeDTO> getEmployeesByName(String name) {
        List<EmployeeModel> employees = employeeRepository.findByName(name);
        return employees.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Método para buscar funcionário pelo sobrenome
    public List<EmployeeDTO> getEmployeesBySurname(String surName) {
        List<EmployeeModel> employees = employeeRepository.findBySurName(surName);
        return employees.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Método para buscar funcionário pelo email
    public EmployeeDTO getEmployeeByEmail(String email) {
        Optional<EmployeeModel> employeeOptional = employeeRepository.findByEmail(email);
        return employeeOptional.map(this::convertToDTO).orElse(null);
    }

    // Método para deletar um funcionário
    public boolean deleteEmployee(Long id) {
        Optional<EmployeeModel> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Método para buscar funcionário pelo username
    public EmployeeModel findByUsername(@NotBlank(message = "Nome de usuário não pode ser vazio") String username) {
        Optional<EmployeeModel> employee = employeeRepository.findByUsername(username);
        return employee.orElse(null); // Retorna o funcionário ou null se não encontrado
    }
}
