package org.zero_consult.people_backend.controllers;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.zero_consult.people_backend.PeopleBackendApplication;
import org.zero_consult.people_backend.entities.*;
import org.zero_consult.people_backend.repositories.CustomerRepository;
import org.zero_consult.people_backend.repositories.CustomerRepository;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PeopleBackendApplication.class)
@AutoConfigureMockMvc
public class CustomerControllerTests {
    private final MockMvc mvc;
    private final CustomerRepository customerRepository;

    public CustomerControllerTests(@Autowired MockMvc mvc, @Autowired CustomerRepository customerRepository) {
        this.mvc = mvc;
        this.customerRepository = customerRepository;
    }
    
    @Test
    public void testCustomerList() throws Exception {
        initData();
        mvc.perform(MockMvcRequestBuilders.get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("jane.doe@x.com")));
    }

    @Test
    public void testAddCustomer() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("json_input/AddCustomer.json").getFile()), "UTF-8")))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testGetCustomer() throws Exception {
        Customer data = initData();
        mvc.perform(MockMvcRequestBuilders.get("/customers/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        Customer data = initData();
        mvc.perform(MockMvcRequestBuilders.put("/customers/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("json_input/UpdateCustomer.json").getFile()), "UTF-8")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        Customer data = initData();
        mvc.perform(MockMvcRequestBuilders.delete("/customers/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testDeleteCustomerEntityHasTimesheetEntriesException() throws Exception {
        Customer data = initData(true);
        mvc.perform(MockMvcRequestBuilders.delete("/customers/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testDeleteCustomerEntityNotFoundException() throws Exception {
        initData();
        mvc.perform(MockMvcRequestBuilders.delete("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testUpdateHasTimesheetEntries() throws Exception {
        Customer data = initData();
        mvc.perform(MockMvcRequestBuilders.post("/customers/" + data.getId() + "/hasTimesheetEntries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hasTimesheetEntries\": true}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private Customer initData() {
        return initData(false);
    }

    private Customer initData(boolean hasTimesheetEntries) {
        Customer entity = new Customer();
        entity.setContactPersonFirstName("Jane");
        entity.setContactPersonLastName("Doe");
        entity.setCompanyName("Company X");
        entity.setCity("Leuven");
        entity.setEmail("jane.doe@x.com");
        entity.setHasTimesheetEntries(hasTimesheetEntries);
        entity.setHiringRatePerHour(100.00);
        entity.setSector(Sector.EDUCATION);
        entity.setPhone("1234567890");
        try {
            entity.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2026-07-20"));
        } catch (ParseException ignore) {
        }
        entity.setStatus(CustomerStatus.ACTIVE);
        entity.setWebsite("www.companyx.com");
        return customerRepository.save(entity);
    }
}
