package org.zero_consult.people_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zero_consult.people_backend.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
