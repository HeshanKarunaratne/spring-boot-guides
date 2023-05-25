package com.example.jparelationships.subject;

import com.example.jparelationships.student.Student;
import com.example.jparelationships.student.StudentRepository;
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
        return null;
    }
}