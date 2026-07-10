package org.zero_consult.people_backend.services;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zero_consult.people_backend.entities.Employee;
import org.zero_consult.people_backend.exceptions.CircularManagerException;
import org.zero_consult.people_backend.exceptions.EmployeeNotFoundException;
import org.zero_consult.people_backend.repositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee addEmployee(Employee entity) throws CircularManagerException {
        if(entity.getManager() != null) {
            Optional<Employee> managerById = employeeRepository.findById(entity.getManager().getId());
            managerById.ifPresent(entity::setManager);
        }
        checkCircularReferences(entity);
        employeeRepository.save(entity);
        return null;
    }

    public Employee updateEmployee(String id, Employee entity) throws CircularManagerException {
        Optional<Employee> employeeById = employeeRepository.findById(id);
        if(employeeById.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        Employee employee = employeeById.get();
        employee.setDepartment(entity.getDepartment());
        employee.setEmail(entity.getEmail());
        employee.setFirstName(entity.getFirstName());
        employee.setFunctionTitle(entity.getFunctionTitle());
        employee.setLastName(entity.getLastName());
        employee.setManager(null);
        if(entity.getManager() != null) {
            Optional<Employee> managerById = employeeRepository.findById(entity.getManager().getId());
            managerById.ifPresent(employee::setManager);
        }
        employee.setPhone(entity.getPhone());
        employee.setStartDate(entity.getStartDate());
        employee.setStatus(entity.getStatus());
        checkCircularReferences(employee);
        return employeeRepository.save(employee);
    }

    private void checkCircularReferences(Employee entity) throws CircularManagerException {
        checkCircularReferences(entity, new ArrayList<>());
    }

    private void checkCircularReferences(Employee entity, List<String> ids) throws CircularManagerException {
        if(ids.contains(entity.getId())) {
            throw new CircularManagerException("Circular reference detected");
        }
        ids.add(entity.getId());
        if(entity.getManager() != null) {
            checkCircularReferences(entity.getManager(), ids);
        }
    }

    public Employee getEmployee(String id) throws EmployeeNotFoundException {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
    }
}
