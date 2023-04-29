package com.example.employeeservice.controller;

import com.example.employeeservice.model.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeRepository repository;
    private final static Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Employee add(@RequestBody Employee employee) {
        LOGGER.info("Employee add: {}", employee);
        return repository.addEmployee(employee);
    }

    @GetMapping
    public List<Employee> findAll() {
        LOGGER.info("Employee find");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable Long id) {
        LOGGER.info("Employee findById: {}", id);
        return repository.findById(id);
    }

    @GetMapping("/department/{departmentId}")
    public List<Employee> findByDepartmentId(@PathVariable Long departmentId) {
        LOGGER.info("Employee findById: {}", departmentId);
        return repository.findByDepartmentId(departmentId);
    }
}
