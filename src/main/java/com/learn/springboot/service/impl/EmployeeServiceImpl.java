package com.learn.springboot.service.impl;

import com.learn.springboot.entity.Employee;
import com.learn.springboot.exception.EmailAlreadyExistException;
import com.learn.springboot.repo.EmployeeRepository;
import com.learn.springboot.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmp = employeeRepository.findByEmail(employee.getEmail());
        if(savedEmp.isPresent()) {
            throw new EmailAlreadyExistException("Employee already exist with given email : " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updated) {
        return employeeRepository.save(updated);
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }

}
