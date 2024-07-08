package com.learn.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.springboot.entity.Employee;
import com.learn.springboot.repo.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    // No need to use mock the behaviour
    @Test
    @DisplayName("")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();

        // when
        ResultActions response = mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())))
                .andDo(print());
    }

    @Test
    @DisplayName("Test for get All Employee REST API")
    public void givenEmpList_whenGetAllEmp_thenReturnEmpList() throws Exception {
        // given
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("Sudhanshu").lastName("Arya").email("sarya@gmail.com").build());
        employeeList.add(Employee.builder().firstName("kapil").lastName("Kumar").email("kapil@gmail.com").build());

        employeeRepository.saveAll(employeeList);

        // when
        ResultActions response = mockMvc.perform(get("/api/employee"));

        // then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(employeeList.size())));

    }

    @Test
    @DisplayName("")
    public void givenEmpId_whenGetEmpById_thenReturnEmp() throws Exception {
        // given
        Long empId = 1L;
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();

        Employee savedEmp = employeeRepository.save(employee);

        // when
        ResultActions response = mockMvc.perform(get("/api/employee/{id}", savedEmp.getId()));

        // then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    @DisplayName("")
    public void givenInvalidEmpId_whenGetEmpById_thenReturnEmpty() throws Exception {
        // given
        Long empId = 1L;
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when
        ResultActions response = mockMvc.perform(get("/api/employee/{id}", empId));

        // then
        response.andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test // Positive Scenario
    @DisplayName("")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        // given
        long empId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Singh")
                .email("sarya@gmail.com")
                .build();

        Employee save = employeeRepository.save(savedEmployee);

        // when
        ResultActions response = mockMvc.perform(put("/api/employee/{id}", save.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));

    }

    @Test // Positive Scenario
    @DisplayName("")
    public void givenInvalidUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception {
        // given
        long empId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Singh")
                .email("sarya@gmail.com")
                .build();

        Employee save = employeeRepository.save(savedEmployee);

        // when
        ResultActions response = mockMvc.perform(put("/api/employee/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("")
    public void givenEmployeeId_whenDeleteEmp_thenReturn204() throws Exception {
        // given
        Employee emp = Employee.builder()
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();
        Employee savedEmp = employeeRepository.save(emp);

        // when
        ResultActions response = mockMvc.perform(delete("/api/employee/{id}", savedEmp.getId()));

        // then
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

}
