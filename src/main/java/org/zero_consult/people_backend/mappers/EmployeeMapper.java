package org.zero_consult.people_backend.mappers;

import org.zero_consult.idl.model.Employee;
import org.zero_consult.people_backend.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeMapper {

    public static Employee toIdl(org.zero_consult.people_backend.entities.Employee employee) {
        return toIdl(employee, new ArrayList<>());
    }

    private static Employee toIdl(org.zero_consult.people_backend.entities.Employee employee, List<String> ids) {
        ids.add(employee.getId());
        Employee mappedEmployee = new Employee();
        mappedEmployee.setId(employee.getId() != null ? Optional.of(employee.getId()) : Optional.empty());
        mappedEmployee.setDepartment(employee.getDepartment() != null ? Optional.of(DepartmentMapper.toIdl(employee.getDepartment())) : Optional.empty());
        mappedEmployee.setEmail(employee.getEmail());
        mappedEmployee.setFirstName(employee.getFirstName());
        mappedEmployee.setFunctionTitle(employee.getFunctionTitle() != null && !employee.getFunctionTitle().isEmpty() ? Optional.of(employee.getFunctionTitle()) : Optional.empty());
        mappedEmployee.setHasTimesheetEntries(Optional.of(employee.isHasTimesheetEntries()));
        mappedEmployee.setLastName(employee.getLastName());
        mappedEmployee.setManager(employee.getManager() != null && !ids.contains(employee.getManager().getId()) ? Optional.of(EmployeeMapper.toIdl(employee.getManager(), ids)) : null);
        mappedEmployee.setPhone(employee.getPhone());
        mappedEmployee.setStartDate(DateUtils.toIdl(employee.getStartDate()));
        mappedEmployee.setStatus(EmployeeStatusMapper.toIdl(employee.getStatus()));
        return mappedEmployee;
    }

    public static org.zero_consult.people_backend.entities.Employee toEntity(Employee employee) {
        org.zero_consult.people_backend.entities.Employee mappedEmployee = new org.zero_consult.people_backend.entities.Employee();
        employee.getDepartment().ifPresent((department) -> mappedEmployee.setDepartment(DepartmentMapper.toEntity(department)));
        mappedEmployee.setEmail(employee.getEmail());
        mappedEmployee.setFirstName(employee.getFirstName());
        employee.getFunctionTitle().ifPresent(mappedEmployee::setFunctionTitle);
        employee.getHasTimesheetEntries().ifPresent(mappedEmployee::setHasTimesheetEntries);
        employee.getId().ifPresent(mappedEmployee::setId);
        mappedEmployee.setLastName(employee.getLastName());
        employee.getManager().ifPresent((manager) -> mappedEmployee.setManager(EmployeeMapper.toEntity(manager)));
        mappedEmployee.setPhone(employee.getPhone());
        mappedEmployee.setStartDate(DateUtils.fromIdl(employee.getStartDate()));
        mappedEmployee.setStatus(employee.getStatus() != null ? EmployeeStatusMapper.toEntity(employee.getStatus()) : null);
        return mappedEmployee;
    }
}
