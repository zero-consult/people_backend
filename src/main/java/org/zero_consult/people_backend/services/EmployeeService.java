package org.zero_consult.people_backend.services;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zero_consult.people_backend.entities.Employee;
import org.zero_consult.people_backend.repositories.EmployeeRepository;

import java.util.List;

@Component
@Transactional
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
