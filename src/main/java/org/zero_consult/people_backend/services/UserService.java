package org.zero_consult.people_backend.services;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zero_consult.people_backend.entities.AppUser;
import org.zero_consult.people_backend.entities.Employee;
import org.zero_consult.people_backend.exceptions.EntityNotFoundException;
import org.zero_consult.people_backend.repositories.UserRepository;

@Component
@Transactional
public class UserService {

    private final EmployeeService employeeService;
    private final UserRepository userRepository;

    public UserService(EmployeeService employeeService, UserRepository userRepository) {
        this.employeeService = employeeService;
        this.userRepository = userRepository;
    }

    public AppUser findByEmail(String email) throws EntityNotFoundException {
        Employee employee = employeeService.getEmployeeByEmail(email);
        AppUser byEmployee = userRepository.findByEmployee(employee);
        if(byEmployee == null) {
            throw new EntityNotFoundException("No user was linked to this employee.");
        }
        return byEmployee;
    }
}
