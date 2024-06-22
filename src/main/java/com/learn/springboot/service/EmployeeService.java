package com.learn.springboot.service;

import com.learn.springboot.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(long id);

    Employee updateEmployee(Employee updated);

    void deleteEmployee(long id);
}
