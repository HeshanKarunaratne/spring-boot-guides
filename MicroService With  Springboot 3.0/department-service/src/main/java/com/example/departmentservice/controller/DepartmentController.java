package com.example.departmentservice.controller;

import com.example.departmentservice.client.EmployeeClient;
import com.example.departmentservice.model.Department;
import com.example.departmentservice.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentRepository repository;
    private final static Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private final EmployeeClient employeeClient;

    public DepartmentController(DepartmentRepository repository, EmployeeClient employeeClient) {
        this.repository = repository;
        this.employeeClient = employeeClient;
    }

    @PostMapping
    public Department add(@RequestBody Department department) {
        LOGGER.info("Department add: {}", department);
        return repository.addDepartment(department);
    }

    @GetMapping
    public List<Department> findAll() {
        LOGGER.info("Department find");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Department findById(@PathVariable Long id) {
        LOGGER.info("Department findById: {}", id);
        return repository.findById(id);
    }

    @GetMapping("/with-employees")
    public List<Department> findAllWithEmployees() {
        LOGGER.info("Department find with employees");
        List<Department> departments = repository.findAll();
        departments.forEach(
                department -> department.setEmployees(
                        employeeClient.findByDepartmentId(department.getId())));
        return departments;
    }

}
