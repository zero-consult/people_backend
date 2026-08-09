package org.zero_consult.people_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PeopleBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeopleBackendApplication.class, args);
    }

}
