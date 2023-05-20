package com.example.methodreference.demo;

import com.example.methodreference.model.EmployeeDO;
import com.example.methodreference.service.EmployeeService;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
public class MethodReferenceMain {
    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeService();
        List<EmployeeDO> employeeDOList = employeeService.loadAndConvert();

        employeeDOList.forEach(System.out::println);
    }
}
