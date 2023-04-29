package com.example.departmentservice.repository;

import com.example.departmentservice.model.Department;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@Repository
public class DepartmentRepository {

    private static List<Department> departments = new ArrayList<>();

    public DepartmentRepository() {
        Department d1 = new Department(1L, "CS");
        Department d2 = new Department(2L, "HR");
        Department d3 = new Department(3L, "Finance");
        departments.add(d1);
        departments.add(d2);
        departments.add(d3);
    }

    public Department addDepartment(Department department) {
        departments.add(department);
        return department;
    }

    public Department findById(Long id) {
        return departments.stream().filter(department -> department.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public List<Department> findAll() {
        return departments;
    }

}
