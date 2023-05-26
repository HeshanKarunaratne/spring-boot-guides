package com.example.jparelationships.subject;

import com.example.jparelationships.student.Student;
import com.example.jparelationships.student.StudentRepository;
import com.example.jparelationships.teacher.Teacher;
import com.example.jparelationships.teacher.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping
    List<Subject> getSubjects() {
        return subjectRepository.findAll();
    }

    @PostMapping
    Subject createSubject(@RequestBody Subject subject) {
        return subjectRepository.save(subject);
    }

    @PutMapping("/{subjectId}/students/{studentId}")
    Subject enrollStudentToSubject(@PathVariable Long subjectId, @PathVariable Long studentId) {
        Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (subjectOptional.isPresent() && studentOptional.isPresent()) {
            Subject subject = subjectOptional.get();
            Student student = studentOptional.get();

            subject.enrollStudent(student);
            return subjectRepository.save(subject);
        }
        throw new IllegalArgumentException("Passed Arguments Illegal");
    }

    @PutMapping("/{subjectId}/teacher/{teacherId}")
    Subject assignTeacherToSubject(@PathVariable Long subjectId, @PathVariable Long teacherId) {
        Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);

        if (subjectOptional.isPresent() && teacherOptional.isPresent()) {
            Subject subject = subjectOptional.get();
            Teacher teacher = teacherOptional.get();

            subject.assignTeacher(teacher);
            return subjectRepository.save(subject);
        }
        throw new IllegalArgumentException("Passed Arguments Illegal");
    }
}