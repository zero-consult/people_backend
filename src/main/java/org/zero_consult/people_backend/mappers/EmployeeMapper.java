package org.zero_consult.people_backend.mappers;

import org.zero_consult.idl.model.Employee;
import org.zero_consult.people_backend.utils.DateUtils;

public class EmployeeMapper {

    public static Employee toIdl(org.zero_consult.people_backend.entities.Employee employee) {
        Employee mappedEmployee = new Employee();
        mappedEmployee.setId(employee.getId());
        mappedEmployee.setDepartment(DepartmentMapper.toIdl(employee.getDepartment()));
        mappedEmployee.setEmail(employee.getEmail());
        mappedEmployee.setFirstName(employee.getFirstName());
        mappedEmployee.setFunctionTitle(employee.getFunctionTitle());
        mappedEmployee.setLastName(employee.getLastName());
        mappedEmployee.setPhone(employee.getPhone());
        mappedEmployee.setStartDate(DateUtils.toIdl(employee.getStartDate()));
        mappedEmployee.setStatus(EmployeeStatusMapper.toIdl(employee.getStatus()));
        return mappedEmployee;
    }
}
