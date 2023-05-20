package com.example.methodreference.service;

import com.example.methodreference.model.Employee;
import com.example.methodreference.model.EmployeeDO;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Heshan Karunaratne
 */
public class EmployeeService {

    public List<Employee> loadEmployees() {
        return IntStream.rangeClosed(1, 10)
                .mapToObj(i -> new Employee(i, "emp" + i, new Random().nextDouble()))
                .collect(Collectors.toList());
    }

    public List<EmployeeDO> loadAndConvert() {
        return IntStream.rangeClosed(1, 10)
                .mapToObj(i -> new Employee(i, "emp" + i, new Random().nextDouble()))
                .map(EmployeeService::convertEmployeeToEmployeeDO)
                .collect(Collectors.toList());
    }

    public static EmployeeDO convertEmployeeToEmployeeDO(Employee employee) {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setId(employee.getId());
        employeeDO.setName(employee.getName());
        employeeDO.setSalary(employee.getSalary());
        return employeeDO;
    }
}
