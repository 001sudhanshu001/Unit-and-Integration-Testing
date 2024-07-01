package com.learn.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.springboot.entity.Employee;
import com.learn.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean // This anno. tell Spring to create mock instance of EmployeeService and add it to app. context,
    // so that it can be injected into EmployeeController
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

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
        BDDMockito.given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

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
    @DisplayName("Junit for get All Employee REST API")
    public void givenEmpList_whenGetAllEmp_thenReturnEmpList() throws Exception {
        // given
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("Sudhanshu").lastName("Arya").email("sarya@gmail.com").build());
        employeeList.add(Employee.builder().firstName("kapil").lastName("Kumar").email("kapil@gmail.com").build());

        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employeeList);

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

        BDDMockito.given(employeeService.getEmployeeById(empId)).willReturn(Optional.of(employee));

        // when
        ResultActions response = mockMvc.perform(get("/api/employee/{id}", empId));

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

        BDDMockito.given(employeeService.getEmployeeById(empId)).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/api/employee/{id}", empId));

        // then
        response.andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test // Positive Scenerio
    @DisplayName("")
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        // given
        long empId = 1L;
        Employee savedEmployee = Employee.builder()
                .id(1L)
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .firstName("Ram")
                .lastName("Singh")
                .email("sarya@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(empId)).willReturn(Optional.of(savedEmployee));
        BDDMockito.given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/api/employee/{id}", empId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));

    }

    @Test // Positive Scenerio
    @DisplayName("")
    public void givenInvalidUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception {
        // given
        long empId = 1L;
        Employee savedEmployee = Employee.builder()
                .id(1L)
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .firstName("Ram")
                .lastName("Singh")
                .email("sarya@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(empId)).willReturn(Optional.empty());
        BDDMockito.given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/api/employee/{id}", empId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("")
    public void givenEmployeeId_whenDeleteEmp_thenReturn204() throws Exception {
        // given
        long empId = 1L;
        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(empId);

        // when
        ResultActions response = mockMvc.perform(delete("/api/employee/{id}", empId));

        // then
        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}
