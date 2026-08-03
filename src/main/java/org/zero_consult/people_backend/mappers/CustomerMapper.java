package org.zero_consult.people_backend.mappers;

import org.zero_consult.idl.model.Customer;
import org.zero_consult.people_backend.utils.DateUtils;

import java.util.Optional;

public class CustomerMapper {
    public static Customer toIdl(org.zero_consult.people_backend.entities.Customer customer) {
        Customer mappedCustomer = new Customer();
        mappedCustomer.setId(customer.getId() != null ? Optional.of(customer.getId()) : Optional.empty());
        mappedCustomer.setCity(customer.getCity() != null ? Optional.of(customer.getCity()) : Optional.empty());
        mappedCustomer.setContactPersonFirstName(customer.getContactPersonFirstName());
        mappedCustomer.setContactPersonLastName(customer.getContactPersonLastName());
        mappedCustomer.setCompanyName(customer.getCompanyName());
        mappedCustomer.setEmail(customer.getEmail());
        mappedCustomer.setHiringRatePerHour(customer.getHiringRatePerHour());
        mappedCustomer.setHasTimesheetEntries(Optional.of(customer.isHasTimesheetEntries()));
        mappedCustomer.setPhone(customer.getPhone());
        mappedCustomer.setStartDate(DateUtils.toIdl(customer.getStartDate()));
        mappedCustomer.setStatus(CustomerStatusMapper.toIdl(customer.getStatus()));
        mappedCustomer.setSector(SectorMapper.toIdl(customer.getSector()));
        return mappedCustomer;
    }

    public static org.zero_consult.people_backend.entities.Customer toEntity(Customer customer) {
        org.zero_consult.people_backend.entities.Customer mappedCustomer = new org.zero_consult.people_backend.entities.Customer();
        customer.getCity().ifPresent(mappedCustomer::setCity);
        mappedCustomer.setCompanyName(customer.getCompanyName());
        mappedCustomer.setContactPersonFirstName(customer.getContactPersonFirstName());
        mappedCustomer.setContactPersonLastName(customer.getContactPersonLastName());
        mappedCustomer.setEmail(customer.getEmail());
        mappedCustomer.setHiringRatePerHour(customer.getHiringRatePerHour());
        customer.getHasTimesheetEntries().ifPresent(mappedCustomer::setHasTimesheetEntries);
        customer.getId().ifPresent(mappedCustomer::setId);
        mappedCustomer.setPhone(customer.getPhone());
        mappedCustomer.setSector(SectorMapper.toEntity(customer.getSector()));
        mappedCustomer.setStartDate(DateUtils.fromIdl(customer.getStartDate()));
        mappedCustomer.setStatus(CustomerStatusMapper.toEntity(customer.getStatus()));
        customer.getWebsite().ifPresent(mappedCustomer::setWebsite);
        return mappedCustomer;
    }
}
