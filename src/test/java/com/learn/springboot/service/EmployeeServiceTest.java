package com.learn.springboot.service;

import com.learn.springboot.entity.Employee;
import com.learn.springboot.exception.EmailAlreadyExistException;
import com.learn.springboot.repo.EmployeeRepository;
import com.learn.springboot.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    Employee employee;

    // Either we can mock an object using @Mock on the Object and @InjectMock to inject this mock or use the static method
    // If we use Annotations to mock then we have to explicitly tell that we are use Annotations to Mock using @ExtendWith
    @BeforeEach
    public void setup() {
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);

         employee = Employee.builder()
                 .id(1L)
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();
    }

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when
        Employee saveEmployee = employeeService.saveEmployee(employee);
        System.out.println(employee);

        // then
        Assertions.assertThat(saveEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        // given
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee)); // This will cause Duplicate email

        // This is stub  as the control will never goes till here so Mockito will throw Exception, so remove this
        // BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when
        org.junit.jupiter.api.Assertions.assertThrows(EmailAlreadyExistException.class, () -> {
           employeeService.saveEmployee(employee);
        });

        // then
        Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("JUnit test for getAll method")
    public void givenEmployeeList_whenGetAllEmployee_thenReturnEmployeeList() {
        Employee employee1 = Employee.builder()
                .firstName("Ajay")
                .lastName("Kumar")
                .email("ajay@gmail.com")
                .build();
        // given
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        // when
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("JUnit test for getAll method(empty list)")
    public void givenEmptyEmployeeList_whenGetAllEmployee_thenReturnEmptyEmployeeList() {
        // given
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("JUnit test for getEmployeeById method")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // given
        BDDMockito.given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        // then
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("Junit for Update Employee method")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("new@gmail.com");
        employee.setFirstName("Vivek");
        
        // when
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("new@gmail.com");
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Vivek");
    }

    @Test
    @DisplayName("Junit for deleteEmployee method")
    public void givenEmployeeId_whenDeleteEmployee_thenDoNothing() {
        // given
        long empId = 1L;
        BDDMockito.willDoNothing().given(employeeRepository).deleteById(1L);

        // when
        employeeService.deleteEmployee(empId);

        // then
        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(empId);
    }
}
