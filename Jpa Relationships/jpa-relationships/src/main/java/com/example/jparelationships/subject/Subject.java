package com.example.jparelationships.subject;

import com.example.jparelationships.student.Student;
import com.example.jparelationships.teacher.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Heshan Karunaratne
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "student_enrolled",
            joinColumns = @JoinColumn(name = "subject_id"), // id of this class(Subject)
            inverseJoinColumns = @JoinColumn(name = "student_id") // id of mapped class(Subject)
    )
    private Set<Student> enrolledStudents = new HashSet<>();

    public void enrollStudent(Student student) {
        enrolledStudents.add(student);
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id") // id of teacher
    private Teacher teacher;

    public void assignTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}