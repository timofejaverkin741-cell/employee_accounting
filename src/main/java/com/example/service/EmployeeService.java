package com.example.service;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> search(String name, Boolean active) {
        boolean hasName = name != null && !name.isBlank();

        if (hasName && active != null) {
            return employeeRepository
                    .findByFullNameContainingIgnoreCaseAndActive(
                            name.trim(), active);
        }

        if (hasName) {
            return employeeRepository
                    .findByFullNameContainingIgnoreCase(name.trim());
        }

        if (active != null) {
            return employeeRepository.findByActive(active);
        }

        return employeeRepository.findAll();
    }

    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Сотрудник с ID " + id + " не найден"));
    }

    public boolean emailExists(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        return employeeRepository.existsByEmailIgnoreCase(
                email.trim());
    }


    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }


    public void delete(Long id) {
        Employee employee = getById(id);
        employeeRepository.delete(employee);
    }
}
