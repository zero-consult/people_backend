package org.zero_consult.people_backend.services;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.zero_consult.people_backend.entities.Customer;
import org.zero_consult.people_backend.entities.Employee;
import org.zero_consult.people_backend.exceptions.EntityHasTimesheetEntriesException;
import org.zero_consult.people_backend.exceptions.EntityNotFoundException;
import org.zero_consult.people_backend.repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Controller
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer addCustomer(Customer entity) {
        entity.setHasTimesheetEntries(false);
        return customerRepository.save(entity);
    }

    public Customer updateCustomer(String id, Customer entity) throws EntityNotFoundException {
        Optional<Customer> customerById = customerRepository.findById(id);
        if (customerById.isEmpty()) {
            throw new EntityNotFoundException("Customer not found");
        }
        Customer customer = customerById.get();
        customer.setCity(entity.getCity());
        customer.setContactPersonFirstName(entity.getContactPersonFirstName());
        customer.setContactPersonLastName(entity.getContactPersonLastName());
        customer.setEmail(entity.getEmail());
        customer.setPhone(entity.getPhone());
        customer.setSector(entity.getSector());
        customer.setStartDate(entity.getStartDate());
        customer.setWebsite(entity.getWebsite());
        return customerRepository.save(customer);
    }

    public Customer getCustomer(String id) throws EntityNotFoundException {
        return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    public Customer updateCustomerHasTimesheetEntries(String id, boolean hasTimesheetEntries) throws EntityNotFoundException {
        Customer customer = getCustomer(id);
        customer.setHasTimesheetEntries(hasTimesheetEntries);
        return customerRepository.save(customer);
    }

    public void deleteCustomer(String id) throws EntityNotFoundException, EntityHasTimesheetEntriesException {
        Customer customer = getCustomer(id);
        if (customer.isHasTimesheetEntries()) {
            throw new EntityHasTimesheetEntriesException("Customer has timesheet entries");
        }
        customerRepository.delete(customer);
    }
}
