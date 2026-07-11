package org.zero_consult.people_backend.mappers;

import org.zero_consult.people_backend.entities.CustomerStatus;

public class CustomerStatusMapper {
    public static org.zero_consult.idl.model.CustomerStatus toIdl(CustomerStatus status) {
        return org.zero_consult.idl.model.CustomerStatus.valueOf(status.name());
    }

    public static CustomerStatus toEntity(org.zero_consult.idl.model.CustomerStatus status) {
        return CustomerStatus.valueOf(status.name());
    }
}
