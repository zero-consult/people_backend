package org.zero_consult.people_backend.controllers;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.zero_consult.people_backend.PeopleBackendApplication;
import org.zero_consult.people_backend.configuration.TestConfig;
import org.zero_consult.people_backend.entities.Department;
import org.zero_consult.people_backend.entities.Employee;
import org.zero_consult.people_backend.entities.EmployeeStatus;
import org.zero_consult.people_backend.repositories.EmployeeRepository;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Import({TestConfig.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PeopleBackendApplication.class)
@AutoConfigureMockMvc
public class EmployeeControllerTests {
    private final MockMvc mvc;
    private final EmployeeRepository employeeRepository;

    public EmployeeControllerTests(@Autowired MockMvc mvc,@Autowired EmployeeRepository employeeRepository) {
        this.mvc = mvc;
        this.employeeRepository = employeeRepository;
    }

    @Test
    public void testEmployeeList() throws Exception {
        initData();
        mvc.perform(MockMvcRequestBuilders.get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("john.doe@x.com")));
    }

    @Test
    public void testAddEmployee() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("json_input/AddEmployee.json").getFile()), "UTF-8")))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testGetEmployee() throws Exception {
        Employee data = initData();
        mvc.perform(MockMvcRequestBuilders.get("/employees/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee data = initData();
        mvc.perform(MockMvcRequestBuilders.put("/employees/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("json_input/UpdateEmployee.json").getFile()), "UTF-8")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Employee data = initData();
        mvc.perform(MockMvcRequestBuilders.delete("/employees/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testDeleteEmployeeEntityHasTimesheetEntriesException() throws Exception {
        Employee data = initData(true);
        mvc.perform(MockMvcRequestBuilders.delete("/employees/" + data.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testDeleteEmployeeEntityNotFoundException() throws Exception {
        initData();
        mvc.perform(MockMvcRequestBuilders.delete("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testUpdateHasTimesheetEntries() throws Exception {
        Employee data = initData();
        mvc.perform(MockMvcRequestBuilders.post("/employees/" + data.getId() + "/hasTimesheetEntries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hasTimesheetEntries\": true}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private Employee initData() {
        return initData(false);
    }

    private Employee initData(boolean hasTimesheetEntries) {
        Employee entity = new Employee();
        entity.setGrossWage(4000.00);
        entity.setDepartment(Department.ENGINEERING);
        entity.setEmail("john.doe@x.com");
        entity.setFirstName("John");
        entity.setFunctionTitle("Software Engineer");
        entity.setHasTimesheetEntries(hasTimesheetEntries);
        entity.setLastName("Doe");
        entity.setPhone("1234567890");
        try {
            entity.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2026-07-20"));
        } catch (ParseException ignore) {
        }
        entity.setStatus(EmployeeStatus.ACTIVE);
        return employeeRepository.save(entity);
    }
}
