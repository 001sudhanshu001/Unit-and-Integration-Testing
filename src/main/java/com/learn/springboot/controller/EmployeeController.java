package com.learn.springboot.controller;

import com.learn.springboot.entity.Employee;
import com.learn.springboot.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long empId) {
        return employeeService.getEmployeeById(empId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable long id, @RequestBody Employee employee) {
        return employeeService.getEmployeeById(id)
                .map(savedEmp -> {
                   savedEmp.setFirstName(employee.getFirstName());
                   savedEmp.setLastName(employee.getLastName());
                   savedEmp.setEmail(employee.getEmail());

                    Employee updatedEmployee = employeeService.updateEmployee(savedEmp);

                    return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteEmplouee( @PathVariable long id) {
        employeeService.deleteEmployee(id);

        return new ResponseEntity<>("Employee deleted successfully ", HttpStatus.NO_CONTENT);
    }
}
