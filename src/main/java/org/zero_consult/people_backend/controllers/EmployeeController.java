package org.zero_consult.people_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.zero_consult.idl.api.EmployeesApi;
import org.zero_consult.idl.model.Employee;
import org.zero_consult.people_backend.mappers.EmployeeMapper;
import org.zero_consult.people_backend.services.EmployeeService;

import java.util.List;

@RestController
public class EmployeeController implements EmployeesApi {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<Employee>> employeesList() {
        return ResponseEntity.ok(
                employeeService
                        .getAllEmployees()
                        .stream()
                        .map(EmployeeMapper::toIdl)
                        .toList());
    }
}
