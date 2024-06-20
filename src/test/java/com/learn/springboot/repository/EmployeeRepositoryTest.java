package com.learn.springboot.repository;

import com.learn.springboot.entity.Employee;
import com.learn.springboot.repo.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Junit test for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();

        // when - action or Behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Junit test for get all employee operation")
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {
        // given
        Employee employee1 = Employee.builder()
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Shyam")
                .lastName("Kumar")
                .email("shyam@gmail.com")
                .build();

        Employee emp1 = employeeRepository.save(employee1);
        Employee emp2 = employeeRepository.save(employee2);

        // when
        List<Employee> employeeList = employeeRepository.findAll();

        // then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Junit test for get employee by id operation")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given
        Employee employee1 = Employee.builder()
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();

        Employee savedEmp = employeeRepository.save(employee1);

        // when
        Employee employeeDB = employeeRepository.findById(savedEmp.getId()).get();

        // then
        assertThat(employeeDB).isNotNull();
    }

    @Test
    @DisplayName("Junit test for get employee by email operation")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        // given
        Employee employee = Employee.builder()
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();

        employeeRepository.save(employee);

        // when
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        // then
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB.getFirstName()).isEqualTo(employee.getFirstName());
    }

    @Test
    @DisplayName("Junit test for Update employee operation")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        String newEmail = "sarya123@gmail.com";
        // given
        Employee employee = Employee.builder()
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when
        Employee savedEmp = employeeRepository.findById(employee.getId()).get();
        savedEmp.setEmail(newEmail);
        Employee updatedEmp = employeeRepository.save(savedEmp);

        // then
        assertThat(updatedEmp.getEmail()).isEqualTo(newEmail);
    }

    @Test
    @DisplayName("Junit test for delete employee operation")
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        // given
        Employee employee = Employee.builder()
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then
        assertThat(employeeOptional).isEmpty();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByFirstNameAndLastName_thenReturnEmployeeObject() {
        // given
        Employee employee = Employee.builder()
                .firstName("Sudhanshu")
                .lastName("Arya")
                .email("sarya@gmail.com")
                .build();
        employeeRepository.save(employee);
        String firstName = "Sudhanshu";
        String lastName = "Arya";

        // when
        Employee savedEmp = employeeRepository.findByFirstNameAndLastNameCustom(firstName, lastName).get();

        // then
        assertThat(savedEmp).isNotNull();
    }

}