package org.zero_consult.people_backend.mappers;

import org.zero_consult.idl.model.Status;
import org.zero_consult.people_backend.entities.EmployeeStatus;

public class EmployeeStatusMapper {
    public static org.zero_consult.idl.model.EmployeeStatus toIdl(EmployeeStatus status) {
        return org.zero_consult.idl.model.EmployeeStatus.fromValue(status.name());
    }

    public static org.zero_consult.people_backend.entities.EmployeeStatus toEntity(Status status) {
        return EmployeeStatus.valueOf(status.name());
    }
}
