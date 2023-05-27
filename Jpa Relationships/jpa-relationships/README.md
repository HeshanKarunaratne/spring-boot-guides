# JPA Relationships

- @OneToOne
- @OneToMany
- @ManyToMany
    
~~~text
Student  >----<  Subject  >----|  Teacher
         m    n           m    1
~~~

- Ex1
    - Students can enroll in many subjects(@ManyToMany)
    - Subject can be learned by many students(@ManyToMany)

~~~java
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonIgnore // to stop iterating
    @ManyToMany(mappedBy = "enrolledStudents") // mapped by fieldName in Subject
    private Set<Subject> subjects = new HashSet<>();
}
~~~

~~~java
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
}
~~~

- Ex2
    - Teacher can teach many subjects(@OneToMany)
    - Subject can be teached by one teacher(@ManyToOne)

~~~java
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id") // id of teacher
    private Teacher teacher;

    public void assignTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
~~~

~~~java
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @JsonIgnore // to stop iterating
    @OneToMany(mappedBy = "teacher")
    private Set<Subject> subjects = new HashSet<>();
}
~~~