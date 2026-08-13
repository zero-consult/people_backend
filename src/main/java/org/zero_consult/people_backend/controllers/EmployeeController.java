package org.zero_consult.people_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.zero_consult.idl.api.EmployeesApi;
import org.zero_consult.idl.model.Employee;
import org.zero_consult.idl.model.UpdateCustomerHasTimesheetEntriesRequest;
import org.zero_consult.people_backend.exceptions.CircularManagerException;
import org.zero_consult.people_backend.exceptions.EntityHasTimesheetEntriesException;
import org.zero_consult.people_backend.exceptions.EntityNotFoundException;
import org.zero_consult.people_backend.exceptions.RestControllerException;
import org.zero_consult.people_backend.mappers.EmployeeMapper;
import org.zero_consult.people_backend.services.EmployeeService;

import java.util.List;

@CrossOrigin(origins = {
        "http://localhost",
        "http://localhost:5173",
        "http://localhost:5174",
        "http://localhost:5175",
        "http://invoices.localhost",
        "http://people.localhost",
        "http://timesheet.localhost",
        "http://invoices.dev.localhost",
        "http://people.dev.localhost",
        "http://timesheet.dev.localhost"
})
@RestController
public class EmployeeController implements EmployeesApi {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<Employee>> employeesList() {
        return ResponseEntity.ok(
                employeeService
                        .getAllEmployees()
                        .stream()
                        .map(EmployeeMapper::toIdl)
                        .toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Employee> addEmployee(Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeMapper.toIdl(employeeService.addEmployee(EmployeeMapper.toEntity(employee))));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Employee> updateEmployee(String id, Employee employee) {
        try {
            return ResponseEntity.ok(EmployeeMapper.toIdl(employeeService.updateEmployee(id, EmployeeMapper.toEntity(employee))));
        } catch (CircularManagerException e) {
            throw new RestControllerException(HttpStatusCode.valueOf(406), e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new RestControllerException(HttpStatusCode.valueOf(404), e.getMessage(), e);
        }
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Employee> getEmployee(String id) {
        try {
            return ResponseEntity.ok(EmployeeMapper.toIdl(employeeService.getEmployee(id)));
        } catch (EntityNotFoundException e) {
            throw new RestControllerException(HttpStatusCode.valueOf(404), e.getMessage(), e);
        }
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Employee> updateEmployeeHasTimesheetEntries(String id, UpdateCustomerHasTimesheetEntriesRequest updateHasTimesheetEntriesRequest) {
        try {
            return ResponseEntity.ok(EmployeeMapper.toIdl(employeeService.updateEmployeeHasTimesheetEntries(id, updateHasTimesheetEntriesRequest.getHasTimesheetEntries())));
        } catch (EntityNotFoundException e) {
            throw new RestControllerException(HttpStatusCode.valueOf(404), e.getMessage(), e);
        }
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteEmployee(String id) {
        try {
            employeeService.deleteEmployee(id);
        } catch (EntityNotFoundException e) {
            throw new RestControllerException(HttpStatusCode.valueOf(404), e.getMessage(), e);
        } catch (EntityHasTimesheetEntriesException e) {
            throw new RestControllerException(HttpStatusCode.valueOf(405), e.getMessage(), e);
        }
        return ResponseEntity.ok("Employee deleted");
    }
}
