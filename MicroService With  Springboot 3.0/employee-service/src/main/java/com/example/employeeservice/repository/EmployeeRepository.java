package com.example.employeeservice.repository;

import com.example.employeeservice.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@Repository
public class EmployeeRepository {

    private static List<Employee> employees = new ArrayList<>();

    public EmployeeRepository() {
        Employee e1 = new Employee(1L, 1L, "Heshan", 28, "ATL");
        Employee e2 = new Employee(2L, 2L, "Dilan", 29, "SE");
        Employee e3 = new Employee(3L, 2L, "Danu", 29, "SSE");
        employees.add(e1);
        employees.add(e2);
        employees.add(e3);
    }

    public Employee addEmployee(Employee employee) {
        employees.add(employee);
        return employee;
    }

    public Employee findById(Long id) {
        return employees.stream().filter(employee -> employee.id().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public List<Employee> findAll() {
        return employees;
    }

    public List<Employee> findByDepartmentId(Long departmentId) {
        return employees.stream().filter(employee -> employee.departmentId().equals(departmentId))
                .toList();
    }
}
