# SpringBoot
- https://spring.io/guides#tutorials

1 . Building a REST Web Service

- Add 'Spring Web' dependency
- Creating Greeting record
~~~text
package com.example.restservice;
public record Greeting(long id, String content) { }
~~~

- Create Controller
~~~java
package com.example.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String TEMPLATE = "Hello, %s! %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name, @RequestParam(value = "feel", defaultValue = "How are you") String feel) {
        return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name, feel));
    }
}
~~~
- A key difference between a traditional MVC controller and the RESTful web service controller shown earlier is the way that the HTTP response body is created. Rather than relying on a view technology to perform server-side rendering of the greeting data to HTML, this RESTful web service controller populates and returns a Greeting object. The object data will be written directly to the HTTP response as JSON.
- This code uses Spring @RestController annotation, which marks the class as a controller where every method returns a domain object instead of a view. It is shorthand for including both @Controller and @ResponseBody.
- Because Jackson 2 is on the classpath, Spring’s MappingJackson2HttpMessageConverter is automatically chosen to convert the Greeting instance to JSON.
- @SpringBootApplication is a convenience annotation that adds all of the following:
    - @Configuration: Tags the class as a source of bean definitions for the application context.
    - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings. For example, if spring-webmvc is on the classpath, this annotation flags the application as a web application and activates key behaviors, such as setting up a DispatcherServlet.
    - @ComponentScan: Tells Spring to look for other components, configurations, and services in the com/example package, letting it find the controllers.
    
~~~java
package com.example.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GreetingController.class)
public class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void greetingShouldReturnMessage() throws Exception {
        String name = "Alice";
        String feel = "Fine";
        long id = 1;
        String message = String.format("Hello, %s! %s!", name, feel);

        Greeting expectedGreeting = new Greeting(id, message);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/greeting")
                .param("name", name)
                .param("feel", feel))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.content").value(containsString(message)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Greeting actualGreeting = new ObjectMapper().readValue(content, Greeting.class);

        Assert.assertEquals(expectedGreeting, actualGreeting);
    }
}
~~~

2 . Scheduling Tasks
- Adding awaitility dependency
~~~text
testImplementation 'org.awaitility:awaitility:3.1.2'
~~~

~~~java
package com.example.schedulingtasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
~~~

- The @EnableScheduling annotation ensures that a background task executor is created. Without it, nothing gets scheduled.
~~~java
package com.example.schedulingtasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchedulingTasksApplication {
	public static void main(String[] args) {
		SpringApplication.run(SchedulingTasksApplication.class, args);
	}
}
~~~

- Add Test class
~~~java
package com.example.schedulingtasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduledTasksTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Test
    public void reportCurrentTime() {
        ScheduledTasks scheduledtasks = Mockito.mock(ScheduledTasks.class);
        scheduledtasks.reportCurrentTime();
        Mockito.verify(scheduledtasks, Mockito.atLeast(1)).reportCurrentTime();
    }

    @Test
    public void reportCurrentTimeTest() throws InterruptedException {
        ScheduledTasks scheduledTasks = new ScheduledTasks();
        scheduledTasks.reportCurrentTime();
        Thread.sleep(5000);
        String currentTime = dateFormat.format(new Date());
        Assertions.assertEquals("The time is now " + currentTime, scheduledTasks.reportCurrentTime());
    }
}
~~~

3 . Consuming a REST Web Service
- RestTemplate makes interacting with most RESTful services a one-line incantation. And it can even bind that data to custom domain types.
- Simple Java record class is annotated with @JsonIgnoreProperties from the Jackson JSON processing library to indicate that any properties not bound in this type should be ignored.
- To directly bind your data to your custom types, you need to specify the variable name to be exactly the same as the key in the JSON document returned from the API. In case your variable name and key in JSON doc do not match, you can use @JsonProperty annotation to specify the exact key of the JSON document.

~~~java
package com.example.consumingrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConsumingRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumingRestApplication.class, args);
    }
}
~~~

~~~java
package com.example.consumingrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
@Service
public class QuoteService {

    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(QuoteService.class);

    @Autowired
    public QuoteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void callRestEndpoint() {
        int random = new Random().nextInt(0,100);

        User user = restTemplate.getForObject(
                "https://jsonplaceholder.typicode.com/posts/" + random, User.class);
        log.info(user.toString());
    }
}
~~~

~~~java
package com.example.consumingrest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
~~~

4 . Building Java Projects with Gradle

- gradle tasks
~~~text
Build tasks
-----------
assemble - Assembles the outputs of this project.
build - Assembles and tests this project.
buildDependents - Assembles and tests this project and all projects that depend on it.
buildNeeded - Assembles and tests this project and all projects it depends on.
classes - Assembles main classes.
clean - Deletes the build directory.
jar - Assembles a jar archive containing the main classes.
testClasses - Assembles test classes.

Build Setup tasks
-----------------
init - Initializes a new Gradle build.
wrapper - Generates Gradle wrapper files.

Documentation tasks
-------------------
javadoc - Generates Javadoc API documentation for the main source code.

Help tasks
----------
buildEnvironment - Displays all buildscript dependencies declared in root project 'Building Java Projects with Gradle'.
dependencies - Displays all dependencies declared in root project 'Building Java Projects with Gradle'.
dependencyInsight - Displays the insight into a specific dependency in root project 'Building Java Projects with Gradle'.
help - Displays a help message.
javaToolchains - Displays the detected java toolchains.
outgoingVariants - Displays the outgoing variants of root project 'Building Java Projects with Gradle'.
projects - Displays the sub-projects of root project 'Building Java Projects with Gradle'.
properties - Displays the properties of root project 'Building Java Projects with Gradle'.
tasks - Displays the tasks runnable from root project 'Building Java Projects with Gradle'.

Verification tasks
------------------
check - Runs all checks.
test - Runs the unit tests.
~~~

- classes: The project’s compiled .class files
- reports: Reports produced by the build (such as test reports)
- libs: Assembled project libraries (usually JAR and/or WAR files)

- You need to add a source for 3rd party libraries.
- The repositories block indicates that the build should resolve its dependencies from the Maven Central repository.
~~~text
repositories { 
    mavenCentral() 
}
~~~

~~~text
jar {
    archiveBaseName = 'gs-gradle'
    archiveVersion =  '0.1.0'
}
~~~
- The Gradle Wrapper is the preferred way of starting a Gradle build
- These scripts allow you to run a Gradle build without requiring that Gradle be installed on your system
 
~~~text
gradle wrapper --gradle-version 6.0.1
~~~
- The Gradle Wrapper is now available for building your project. Add it to your version control system, and everyone that clones your project can build it just the same. It can be used in the exact same way as an installed version of Gradle
~~~text
gradlew build
~~~

- To make this code runnable, we can use gradle’s application plugin. Add this to your build.gradle file.
~~~text
apply plugin: 'application'
mainClassName = 'hello.HelloWorld'
~~~
- Type 'gradlew run'
- Final build.gradle file
~~~gradle
apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'application'

mainClassName = 'hello.HelloWorld'

repositories {
    mavenCentral()
}

jar {
    archiveBaseName = 'gs-gradle'
    archiveVersion =  '0.1.0'
}

sourceCompatibility = 1.8
targetCompatibility = 1.8

dependencies {
    implementation "joda-time:joda-time:2.2"
    testImplementation "junit:junit:4.12"
}
~~~

5 . Building Java Projects with Maven
- Check maven version after adding maven binaries to class path 
~~~text
mvn -v
~~~

- modelVersion: POM model version (always 4.0.0).
- groupId: Group or organization that the project belongs to. Often expressed as an inverted domain name.
- artifactId: Name to be given to the project’s library artifact (for example, the name of its JAR or WAR file).
- version: Version of the project that is being built.
- packaging: How the project should be packaged. Defaults to "jar" for JAR file packaging. Use "war" for WAR file packaging.

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.springframework</groupId>
    <artifactId>gs-maven</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0</version>

    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer
                                        implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>hello.HelloWorld</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
~~~

~~~text
mvn compile
mvn package
java -jar target/gs-maven-0.1.0.jar
~~~

- Add below dependencies
~~~text
<dependencies>
    <dependency>
        <groupId>joda-time</groupId>
        <artifactId>joda-time</artifactId>
        <version>2.9.2</version>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>
</dependencies>
~~~

- Maven uses a plugin called "surefire" to run unit tests. The default configuration of this plugin compiles and runs all classes in src/test/java with a name matching *Test

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	
	<groupId>org.springframework</groupId>
	<artifactId>gs-maven</artifactId>
	<packaging>jar</packaging>
	<version>0.1.0</version>

	<properties>
		<maven.compiler.source>1.8</maven.compiler.source>
		<maven.compiler.target>1.8</maven.compiler.target>
	</properties>

	<dependencies>
		<!-- tag::joda[] -->
		<dependency>
			<groupId>joda-time</groupId>
			<artifactId>joda-time</artifactId>
			<version>2.9.2</version>
		</dependency>
		<!-- end::joda[] -->
		<!-- tag::junit[] -->
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.12</version>
			<scope>test</scope>
		</dependency>
		<!-- end::junit[] -->
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-shade-plugin</artifactId>
				<version>3.2.4</version>
				<executions>
					<execution>
						<phase>package</phase>
						<goals>
							<goal>shade</goal>
						</goals>
						<configuration>
							<transformers>
								<transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
									<mainClass>hello.HelloWorld</mainClass>
								</transformer>
							</transformers>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</project>
~~~

6 . Accessing Relational Data using JDBC with Spring

- Add 'JDBC API' and 'H2 Database' dependencies
~~~gradle
plugins {
	id 'org.springframework.boot' version '3.0.0'
	id 'io.spring.dependency-management' version '1.1.0'
	id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
	runtimeOnly 'com.h2database:h2'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
	useJUnitPlatform()
}
~~~

- Spring Boot supports H2 (an in-memory relational database engine) and automatically creates a connection. Because we use spring-jdbc, Spring Boot automatically creates a JdbcTemplate. The @Autowired JdbcTemplate field automatically loads it and makes it available.
- For single insert statements, the insert method of JdbcTemplate is good. However, for multiple inserts, it is better to use batchUpdate
- Use ? for arguments to avoid SQL injection attacks by instructing JDBC to bind variables.

~~~java
package com.example.relationaldataaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class RelationalDataAccessApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(RelationalDataAccessApplication.class);

	public static void main(String args[]) {
		SpringApplication.run(RelationalDataAccessApplication.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... strings) throws Exception {

		log.info("Creating tables");

		jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE customers(" +
				"id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

		List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long")
				.stream()
				.map(name -> name.split(" "))
				.collect(Collectors.toList());

		splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

		log.info("Querying for customer records where first_name = 'Josh':");
		jdbcTemplate.query(
				"SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
				(rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
		).forEach(customer -> log.info(customer.toString()));
	}
}
~~~

7 . Accessing Relational Data using JDBC with Spring 2
- Add below dependencies
~~~text
plugins {
	id 'org.springframework.boot' version '3.0.0'
	id 'io.spring.dependency-management' version '1.1.0'
	id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	runtimeOnly 'com.h2database:h2'
	runtimeOnly 'org.springframework.boot:spring-boot-devtools'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
	useJUnitPlatform()
}
~~~

- Add below properties
~~~properties
spring.h2.console.enabled=true
spring.jpa.properties.hibernate.generate_statistics=true
logging.level.org.hibernate.stat=debug
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.type=trace
spring.datasource.url=jdbc:h2:mem:testdb
spring.data.jpa.repositories.bootstrap-mode=default
spring.datasource.username=user
spring.datasource.password=123
~~~

~~~java
package com.example.relationaldataaccess;

/**
 * @author Heshan Karunaratne
 */
public class Student {
    private Long id;
    private String name;
    private String passportNumber;

    public Student() {
        super();
    }

    public Student(Long id, String name, String passportNumber) {
        super();
        this.id = id;
        this.name = name;
        this.passportNumber = passportNumber;
    }

    public Student(String name, String passportNumber) {
        super();
        this.name = name;
        this.passportNumber = passportNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public String toString() {
        return String.format("Student [id=%s, name=%s, passportNumber=%s]", id, name, passportNumber);
    }

}
~~~

~~~java
package com.example.relationaldataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@Repository
public class StudentJdbcRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Student findById(long id) {
        return jdbcTemplate.queryForObject("select * from student where id=?", new BeanPropertyRowMapper<>(Student.class), id);
    }

    static class StudentRowMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student student = new Student();
            student.setId(rs.getLong("id"));
            student.setName(rs.getString("name"));
            student.setPassportNumber(rs.getString("passport_number"));
            return student;
        }
    }

    public List<Student> findAll() {
        return jdbcTemplate.query("select * from student", new StudentRowMapper());
    }

    public void deleteById(long id) {
        jdbcTemplate.update("delete from student where id=?", id);
    }

    public int insert(Student student) {
        return jdbcTemplate.update("insert into student (id, name, passport_number) " + "values(?,  ?, ?)",
                student.getId(), student.getName(), student.getPassportNumber());
    }

    public int update(Student student) {
        return jdbcTemplate.update("update student " + " set name = ?, passport_number = ? " + " where id = ?",
                student.getName(), student.getPassportNumber(), student.getId());
    }
}
~~~

~~~java
package com.example.relationaldataaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RelationalDataAccessApplication implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StudentJdbcRepository repository;
    public static void main(String args[]) {
        SpringApplication.run(RelationalDataAccessApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        logger.info("Inserting -> {}", repository.insert(new Student(10010L, "John", "A1234657")));
        logger.info("Update 10001 -> {}", repository.update(new Student(10001L, "Name-Updated", "New-Passport")));
        repository.deleteById(10002L);
        logger.info("All users -> {}", repository.findAll());
    }
}
~~~

8 . Uploading Files

- File Upload Controller
~~~java
package com.example.uploadingfiles;

import com.example.uploadingfiles.storage.StorageFileNotFoundException;
import com.example.uploadingfiles.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author Heshan Karunaratne
 */
@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
~~~

- StorageService Interface
~~~java
package com.example.uploadingfiles.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void store(MultipartFile file);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

}
~~~

- Add validation properties 
~~~properties
spring.servlet.multipart.max-file-size=128KB
spring.servlet.multipart.max-request-size=128KB
~~~
- spring.servlet.multipart.max-file-size is set to 128KB, meaning total file size cannot exceed 128KB.
- spring.servlet.multipart.max-request-size is set to 128KB, meaning total request size for a multipart/form-data cannot exceed 128KB.

- Main Application
~~~java
package com.example.uploadingfiles;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.uploadingfiles.storage.StorageProperties;
import com.example.uploadingfiles.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class UploadingFilesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadingFilesApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}
~~~

9 . Authenticating a User with LDAP
- @RestController tells Spring MVC to write the text directly into the HTTP response body, because there are no views. Instead, when you visit the page, you get a simple message in the browser

~~~text
plugins {
	id 'org.springframework.boot' version '3.0.0'
	id 'io.spring.dependency-management' version '1.1.0'
	id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.ldap:spring-ldap-core")
	implementation("org.springframework.security:spring-security-ldap")
	implementation("com.unboundid:unboundid-ldapsdk")

	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation("org.springframework.security:spring-security-test")
}

test {
	useJUnitPlatform()
}
~~~

~~~java
package com.example.authenticatingldap;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;


@Configuration
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().fullyAuthenticated()
				.and()
			.formLogin();

		return http.build();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.ldapAuthentication()
				.userDnPatterns("uid={0},ou=people")
				.groupSearchBase("ou=groups")
				.contextSource()
					.url("ldap://localhost:8389/dc=springframework,dc=org")
					.and()
				.passwordCompare()
					.passwordEncoder(new BCryptPasswordEncoder())
					.passwordAttribute("userPassword");
	}

}
~~~

- Add below properties to application.properties
~~~properties
spring.ldap.embedded.ldif=classpath:test-server.ldif
spring.ldap.embedded.base-dn=dc=springframework,dc=org
spring.ldap.embedded.port=8389
~~~

10 . Messaging with Redis
- Navigate to 'C:\Program Files\Redis' and type 'redis-cli'

~~~java
package com.example.messagingredis;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private AtomicInteger counter = new AtomicInteger();

    public void receiveMessage(String message) {
        LOGGER.info("Received <" + message + ">");
        counter.incrementAndGet();
    }

    public int getCount() {
        return counter.get();
    }
}
~~~

- Spring Data Redis provides all the components you need to send and receive messages with Redis. Specifically, you need to configure:
  - Connection factory
  - Message listener container
  - Redis template
  
- This example uses Spring Boot’s default RedisConnectionFactory, an instance of JedisConnectionFactory that is based on the Jedis Redis library. The connection factory is injected into both the message listener container and the Redis template, as the following example

~~~java
package com.example.messagingredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@SpringBootApplication
public class MessagingRedisApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessagingRedisApplication.class);

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	Receiver receiver() {
		return new Receiver();
	}

	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	public static void main(String[] args) throws InterruptedException {

		ApplicationContext ctx = SpringApplication.run(MessagingRedisApplication.class, args);

		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		Receiver receiver = ctx.getBean(Receiver.class);

		while (receiver.getCount() == 0) {

			LOGGER.info("Sending message...");
			template.convertAndSend("chat", "Hello from Redis!");
			Thread.sleep(500L);
		}

		System.exit(0);
	}
}
~~~

- The bean defined in the listenerAdapter method is registered as a message listener in the message listener container defined in container and will listen for messages on the chat topic. Because the Receiver class is a POJO, it needs to be wrapped in a message listener adapter that implements the MessageListener interface (which is required by addMessageListener()). The message listener adapter is also configured to call the receiveMessage() method on Receiver when a message arrives.
- The connection factory and message listener container beans are all you need to listen for messages. To send a message, you also need a Redis template. Here, it is a bean configured as a StringRedisTemplate, an implementation of RedisTemplate that is focused on the common use of Redis, where both keys and values are String instances.
- The main() method kicks off everything by creating a Spring application context. The application context then starts the message listener container, and the message listener container bean starts listening for messages. The main() method then retrieves the StringRedisTemplate bean from the application context and uses it to send a Hello from Redis! message on the chat topic. Finally, it closes the Spring application context, and the application ends.

11 . Messaging with RabbitMQ
- RabbitMQ is an AMQP(Advanced Message Queuing Protocol) server
- Docker command
    - docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.11-management
- Access RabbitMQ Management ui
    - http://localhost:15672/
    - username: guest
    - password: guest
    
~~~java
package com.example.messagingrabbitmq;

import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(String message) {
    System.out.println("Received <" + message + ">");
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}
~~~
- Spring AMQP’s RabbitTemplate provides everything you need to send and receive messages with RabbitMQ
    - Configure a message listener container
    - Declare the queue, the exchange, and the binding between them
    - Configure a component to send some messages to test the listener
    
~~~java
package com.example.messagingrabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MessagingRabbitmqApplication {

  static final String topicExchangeName = "spring-boot-exchange";

  static final String queueName = "spring-boot";

  @Bean
  Queue queue() {
    return new Queue(queueName, false);
  }

  @Bean
  TopicExchange exchange() {
    return new TopicExchange(topicExchangeName);
  }

  @Bean
  Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(Receiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(MessagingRabbitmqApplication.class, args).close();
  }

}
~~~

12 . Resillience4J
- Will be following below topics
    - Circuit Breakers
    - Retry
    - Rate Limiter: Define how many requests can be served in a period

- Check below topics as well
    - Time Limiter
    - Cache

~~~text
CLOSE: Flow is working correctly without an issue
OPEN: Failure rate above threshold
HALF_OPEN: Waiting duration, can go to CLOSED or OPEN states. When it is in the HALF_OPEN still the failure rate is above given threshold goes to OPEN state or else go to CLOSED state
~~~

- Generate 2 Springboot projects
    - ProjectA: Web and Resillience4J
    - ProjectB: Web

- Implementing Circuit Breaker
    - Add below dependencies to ProjectA
    ~~~text
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-aop</artifactId>
		</dependency>
    ~~~

    - Add below code to application.yml for actuator configs
    ~~~yml
    management:
        health:
            circuitbreakers:
            enabled: true
        endpoints:
            web:
            exposure:
                include: health
        endpoint:
            health:
            show-details: always
    ~~~

    - Add below code application.yml for circuit breaker
    ~~~yml
    server:
      port: 8080
    
    management:
      health:
        circuitbreakers:
          enabled: true
      endpoints:
        web:
          exposure:
            include: health
      endpoint:
        health:
          show-details: always
    
    resilience4j:
      circuitbreaker:
        instances:
          serviceA:
            registerHealthIndicator: true
            eventConsumerBufferSize: 10
            failureRateThreshold: 50
            minimumNumberOfCalls: 5
            automaticTransitionFromOpenToHalfOpenEnabled: true
            waitDurationInOpenState: 5s
            permittedNumberOfCallsInHalfOpenState: 3
            slidingWindowSize: 10
            slidingWindowType: COUNT_BASED
      retry:
        instances:
          serviceA:
            registerHealthIndicator: true
            maxRetryAttempts: 5
            waitDuration: 10s
      ratelimiter:
        instances:
          serviceA:
            registerHealthIndicator: true
            limitForPeriod: 10
            limitRefreshPeriod: 10s
            timeoutDuration: 3s
    ~~~

13 . Distributed Transaction
~~~txt
Monolith -> SOA(Service Oriented Architecture -> Microservice)
~~~

- Bad practices with MicroService and Distributed Systems
    1. Connecting multiple Microservices to a single DB
    2. Replicating DB instance between Microservices
    3. This can be solved using 2 Phase Commit or SAGA pattern

- 2 Phase Commit(Prepare and commit)
    - Advantages
        1. Strong consistency with timeouts
        2. 
    - Disadvantages
        1. Co-ordinator handles all the microservices and the transaction
        2. Resources are locked from the prepare state
        3. Using http requests that slows the entire process
        4. Latency issues
        
- Problems in 2 Phase commit
    1. What happens if the co-ordinator fails?
    2. What happens if the microservice fails to reply during the phase 1 co-ordinator doesn't even know the state of the failed microservice
    3. What if the microservice fails during commit phase?
    
    - In all of the above cases the resource is blocked and no others can use that resource
    - Modified version of 2 phase commit is c  alled 3 phase commit

- 3 Phase Commit
    1. Can commit phase: Co-ordinator finds how many participants are there
    2. Pre commit phase: Prepare the data and lock resources
    3. Do commit phase: Checks for the acknowledgement of all the participants
    
- Saga Design Pattern
    - Is a way to manage data consistency across microservices in distributed transaction scenarios
    ![Diagram](Saga%20Pattern/resources/saga.PNG "Diagram")

- Start the axon server
    - java -jar axonserver.jar 

- Steps
    1. Create OrderService, PaymentService, ShipmentService, UserService and CommonService
        - Add web, lombok, jpa and H2 dependencies 
        - Add below dependencies
        ~~~txt
            <dependency>
                <groupId>org.axonframework</groupId>
                <artifactId>axon-spring-boot-starter</artifactId>
                <version>4.7.3</version>
            </dependency>
            <dependency>
                <groupId>com.google.guava</groupId>
                <artifactId>guava</artifactId>
                <version>31.1-jre</version>
            </dependency>
        ~~~
    2. Add below dependency to all except CommonService
        ~~~text
            <dependency>
                <groupId>com.example</groupId>
                <artifactId>CommonService</artifactId>
                <version>0.0.1-SNAPSHOT</version>
            </dependency>
        ~~~