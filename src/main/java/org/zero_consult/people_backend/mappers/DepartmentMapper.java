package org.zero_consult.people_backend.mappers;

import org.zero_consult.idl.model.Department;

public class DepartmentMapper {
    public static Department toIdl(org.zero_consult.people_backend.entities.Department department) {
        return Department.valueOf(department.name());
    }

    public static org.zero_consult.people_backend.entities.Department toEntity(Department department) {
        return org.zero_consult.people_backend.entities.Department.valueOf(department.name());
    }
}
