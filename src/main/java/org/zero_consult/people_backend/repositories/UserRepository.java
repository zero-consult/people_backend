package org.zero_consult.people_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zero_consult.people_backend.entities.Employee;
import org.zero_consult.people_backend.entities.AppUser;

public interface UserRepository extends JpaRepository<AppUser, String> {

    AppUser findByEmployee(Employee employee);
}
