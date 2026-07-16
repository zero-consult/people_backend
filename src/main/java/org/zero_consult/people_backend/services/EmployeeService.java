package org.zero_consult.people_backend.services;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zero_consult.people_backend.entities.Employee;
import org.zero_consult.people_backend.exceptions.CircularManagerException;
import org.zero_consult.people_backend.exceptions.EntityHasTimesheetEntriesException;
import org.zero_consult.people_backend.exceptions.EntityNotFoundException;
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

    public Employee addEmployee(Employee entity) {
        if (entity.getManager() != null) {
            Optional<Employee> managerById = employeeRepository.findById(entity.getManager().getId());
            managerById.ifPresent(entity::setManager);
        }
        entity.setHasTimesheetEntries(false);
        return employeeRepository.save(entity);
    }

    public Employee updateEmployee(String id, Employee entity) throws CircularManagerException, EntityNotFoundException {
        Optional<Employee> employeeById = employeeRepository.findById(id);
        if (employeeById.isEmpty()) {
            throw new EntityNotFoundException("Employee not found");
        }
        Employee employee = employeeById.get();
        employee.setDepartment(entity.getDepartment());
        employee.setEmail(entity.getEmail());
        employee.setFirstName(entity.getFirstName());
        employee.setFunctionTitle(entity.getFunctionTitle());
        employee.setLastName(entity.getLastName());
        employee.setManager(null);
        if (entity.getManager() != null) {
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
        if (ids.contains(entity.getId())) {
            throw new CircularManagerException("Circular reference detected. Another manager is required.");
        }
        ids.add(entity.getId());
        if (entity.getManager() != null) {
            checkCircularReferences(entity.getManager(), ids);
        }
    }

    public Employee getEmployee(String id) throws EntityNotFoundException {
        return employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    public Employee updateEmployeeHasTimesheetEntries(String id, boolean hasTimesheetEntries) throws EntityNotFoundException {
        Employee employee = getEmployee(id);
        employee.setHasTimesheetEntries(hasTimesheetEntries);
        return employeeRepository.save(employee);
    }


    public void deleteEmployee(String id) throws EntityNotFoundException, EntityHasTimesheetEntriesException {
        Employee employee = getEmployee(id);
        if (employee.isHasTimesheetEntries()) {
            throw new EntityHasTimesheetEntriesException("Employee has timesheet entries");
        }
        employeeRepository.delete(employee);
    }
}
