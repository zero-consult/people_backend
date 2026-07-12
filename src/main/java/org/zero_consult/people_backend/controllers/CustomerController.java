package org.zero_consult.people_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.zero_consult.idl.api.CustomersApi;
import org.zero_consult.idl.model.Customer;
import org.zero_consult.people_backend.exceptions.EntityNotFoundException;
import org.zero_consult.people_backend.mappers.CustomerMapper;
import org.zero_consult.people_backend.services.CustomerService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5100", "http://localhost:5101"})
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
}
