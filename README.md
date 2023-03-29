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

6 . 