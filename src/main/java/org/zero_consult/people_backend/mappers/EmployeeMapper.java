package org.zero_consult.people_backend.mappers;

import org.zero_consult.idl.model.Employee;
import org.zero_consult.people_backend.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {

    public static Employee toIdl(org.zero_consult.people_backend.entities.Employee employee) {
        return toIdl(employee, new ArrayList<>());
    }

    private static Employee toIdl(org.zero_consult.people_backend.entities.Employee employee, List<String> ids) {
        ids.add(employee.getId());
        Employee mappedEmployee = new Employee();
        mappedEmployee.setId(employee.getId());
        mappedEmployee.setDepartment(DepartmentMapper.toIdl(employee.getDepartment()));
        mappedEmployee.setEmail(employee.getEmail());
        mappedEmployee.setFirstName(employee.getFirstName());
        mappedEmployee.setFunctionTitle(employee.getFunctionTitle());
        mappedEmployee.setLastName(employee.getLastName());
        mappedEmployee.setManager(employee.getManager() != null && !ids.contains(employee.getManager().getId()) ? EmployeeMapper.toIdl(employee.getManager(), ids) : null);
        mappedEmployee.setPhone(employee.getPhone());
        mappedEmployee.setStartDate(DateUtils.toIdl(employee.getStartDate()));
        mappedEmployee.setStatus(EmployeeStatusMapper.toIdl(employee.getStatus()));
        return mappedEmployee;
    }

    public static org.zero_consult.people_backend.entities.Employee toEntity(Employee employee) {
        org.zero_consult.people_backend.entities.Employee mappedEmployee = new org.zero_consult.people_backend.entities.Employee();
        mappedEmployee.setDepartment(employee.getDepartment() != null ? DepartmentMapper.toEntity(employee.getDepartment()) : null);
        mappedEmployee.setEmail(employee.getEmail());
        mappedEmployee.setFirstName(employee.getFirstName());
        mappedEmployee.setFunctionTitle(employee.getFunctionTitle());
        mappedEmployee.setLastName(employee.getLastName());
        mappedEmployee.setManager(employee.getManager() != null ? EmployeeMapper.toEntity(employee.getManager()) : null);
        mappedEmployee.setPhone(employee.getPhone());
        mappedEmployee.setStartDate(DateUtils.fromIdl(employee.getStartDate()));
        mappedEmployee.setStatus(employee.getStatus() != null ? EmployeeStatusMapper.toEntity(employee.getStatus()) : null);
        return mappedEmployee;
    }
}
