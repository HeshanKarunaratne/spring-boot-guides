- Enable Eureka Server

- Make sure to add a DNS record when using the API Gateway
    - eg: C:\Windows\System32\drivers\etc\hosts
    - 127.0.0.1       SL-HKarunaratne.mitrai.local

~~~text
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
~~~

~~~java
package com.example.serviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistryApplication.class, args);
	}

}
~~~

- Make sure to add these properties so that the eureka server will not be treated as a client
~~~yaml
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
server:
  port: 8761
~~~

- Gateway Service

~~~text
implementation 'org.springframework.cloud:spring-cloud-starter-gateway'
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
~~~

~~~java
package com.example.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

}
~~~

- Add below properties so that the gateway service can identify below routes
~~~yaml
spring:

# Gateway service name that will be displayed in eureka server
  application:
    name: SWIGGY-GATEWAY
  
  cloud:
    gateway:
      routes:
        - id: swiggy-app
# To enable High Availability to SWIGGY-APP application, can change to http if need only 1 instance          
          uri: lb://SWIGGY-APP
          predicates:
            - Path=/api/swiggy/**

        - id: restaurant-service
          uri: lb://RESTAURANT-SERVICE
          predicates:
            - Path=/api/restaurant/**

# All the clients need to identify eureka server
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
~~~

- Restaurant Service 

~~~yaml
server:
  port: 8082

spring:
  application:
    name: RESTAURANT-SERVICE

eureka:
  client:
    service-url:
      defaultZone : http://localhost:8761/eureka/
~~~

- Swiggy Service

~~~yaml
server:
  port: 8081

spring:
  application:
    name: SWIGGY-APP

eureka:
  client:
    service-url:
      defaultZone : http://localhost:8761/eureka/
~~~

- Auth Service 
    - Create an entity, controller, service, repository and config packages
    - We need to create 3 endpoints
        - Adding a new user(/register)
        - Generating jwt token(/token)
        - Validating jwt token(/validateToken)
    - We can create new users using the /register api
    - If we try to get a token using a username and a password we would get a token for any request body we send
    - To avoid that we need to have a Bean of AuthenticationManager
    - Having an AuthenticationManager Bean will not help to resolve the issue, for that we need to have UserDetailsService Bean - CustomUserDetailsService class
    - We need to override the loadUserByUsername and need to return an Object of UserDetails
    - We need to have a Bean of AuthenticationProvider as well
    
~~~java
package com.example.authservice.config;

import com.example.authservice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Heshan Karunaratne
 */
@Configuration
@EnableWebSecurity
public class AuthConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/register", "/auth/token", "/auth/validate").permitAll()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
~~~

~~~java
package com.example.authservice.config;

import com.example.authservice.entity.UserCredential;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Heshan Karunaratne
 */
public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;

    public CustomUserDetails(UserCredential userCredential) {
        this.username = userCredential.getName();
        this.password = userCredential.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
~~~

~~~java
package com.example.authservice.service;

import com.example.authservice.config.CustomUserDetails;
import com.example.authservice.entity.UserCredential;
import com.example.authservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Heshan Karunaratne
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> credential = repository.findByName(username);
        return credential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
    }
}
~~~

~~~java
package com.example.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Heshan Karunaratne
 */
@Service
public class JWTService {
    public static final String SECRET = "432A462D4A614E645267556B58703273357538782F413F4428472B4B62506553";

    /**
     * Generate Token Code
     * @param userName
     * @return
     */
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Validate Token
     * @param token
     * @return
     */
    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    private Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }

}
~~~