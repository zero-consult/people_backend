package org.zero_consult.people_backend.mappers;

import org.zero_consult.people_backend.entities.Sector;

public class SectorMapper {
    public static org.zero_consult.idl.model.Sector toIdl(Sector status) {
        return org.zero_consult.idl.model.Sector.valueOf(status.name());
    }

    public static Sector toEntity(org.zero_consult.idl.model.Sector status) {
        return Sector.valueOf(status.name());
    }
}
