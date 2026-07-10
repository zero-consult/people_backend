package org.zero_consult.people_backend.mappers;

import org.zero_consult.people_backend.entities.EmployeeStatus;

public class EmployeeStatusMapper {
    public static org.zero_consult.idl.model.EmployeeStatus toIdl(EmployeeStatus status) {
        return org.zero_consult.idl.model.EmployeeStatus.valueOf(status.name());
    }

    public static EmployeeStatus toEntity(org.zero_consult.idl.model.EmployeeStatus status) {
        return EmployeeStatus.valueOf(status.name());
    }
}
