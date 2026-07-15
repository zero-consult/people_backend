package org.zero_consult.people_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.zero_consult.idl.api.CustomersApi;
import org.zero_consult.idl.model.Customer;
import org.zero_consult.idl.model.Employee;
import org.zero_consult.idl.model.UpdateCustomerHasTimesheetEntriesRequest;
import org.zero_consult.people_backend.exceptions.EntityHasTimesheetEntriesException;
import org.zero_consult.people_backend.exceptions.EntityNotFoundException;
import org.zero_consult.people_backend.mappers.CustomerMapper;
import org.zero_consult.people_backend.mappers.EmployeeMapper;
import org.zero_consult.people_backend.services.CustomerService;

import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174",
        "http://people.localhost",
        "http://timesheet.localhost",
        "http://people.dev.localhost",
        "http://timesheet.dev.localhost",
        "http://people.tst.localhost",
        "http://timesheet.tst.localhost"
})
@RestController
public class CustomerController implements CustomersApi {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<Customer> addCustomer(Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerMapper.toIdl(customerService.addCustomer(CustomerMapper.toEntity(customer))));
    }

    @Override
    public ResponseEntity<List<Customer>> customersList() {
        return ResponseEntity.ok(
                customerService
                        .getAllCustomers()
                        .stream()
                        .map(CustomerMapper::toIdl)
                        .toList());
    }

    @Override
    public ResponseEntity<Customer> getCustomer(String id) {
        try {
            return ResponseEntity.ok(CustomerMapper.toIdl(customerService.getCustomer(id)));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @Override
    public ResponseEntity<Customer> updateCustomer(String id, Customer customer) {
        try {
            return ResponseEntity.ok(CustomerMapper.toIdl(customerService.updateCustomer(id, CustomerMapper.toEntity(customer))));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @CrossOrigin(origins = {})
    @Override
    public ResponseEntity<Customer> updateCustomerHasTimesheetEntries(String id, UpdateCustomerHasTimesheetEntriesRequest updateHasTimesheetEntriesRequest) {
        try {
            return ResponseEntity.ok(CustomerMapper.toIdl(customerService.updateCustomerHasTimesheetEntries(id, updateHasTimesheetEntriesRequest.getHasTimesheetEntries())));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @Override
    public ResponseEntity<String> deleteCustomer(String id) {
        try {
            customerService.deleteCustomer(id);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e); // TODO
        } catch (EntityHasTimesheetEntriesException e) {
            throw new RuntimeException(e); // TODO
        }
        return ResponseEntity.ok("Customer deleted");
    }
}
