/**
 * This lesson implements the modern, standard way to secure a stateless REST API:
 * **JSON Web Tokens (JWT)**.
 *
 * ## The Problem with HTTP Basic Auth
 *
 * HTTP Basic Authentication (from the last lesson) is simple but requires the client
 * to send the username and password with **every single request**. This is inefficient
 * and less secure.
 *
 * ## The Solution: JWT-based Authentication
 *
 * JWT is a token-based, stateless authentication mechanism. The flow is:
 * 1.  **Login**: The client sends their username and password to a `/login` endpoint.
 * 2.  **Token Generation**: If the credentials are valid, the server generates a JWT—a
 *     digitally signed, encoded string containing user information (like username and roles).
 * 3.  **Token Storage**: The server sends this token back to the client. The client stores it.
 * 4.  **Authenticated Requests**: For all subsequent requests to protected endpoints, the client
 *     includes the JWT in the `Authorization` header (e.g., `Authorization: Bearer <token>`).
 * 5.  **Token Validation**: The server validates the token's signature and expiration. If valid,
 *     it trusts the information inside and processes the request.
 *
 * This is **stateless**: the server doesn't need to store session information, making it
 * highly scalable.
 *
 * ## PREQUISITES: Maven `pom.xml` Dependencies
 * In addition to `spring-boot-starter-security` and `spring-boot-starter-web`, we need a
 * library to create and parse JWTs. `jjwt` is a popular choice.
 * ```xml
 * <!-- JWT Support -->
 * <dependency>
 *     <groupId>io.jsonwebtoken</groupId>
 *     <artifactId>jjwt-api</artifactId>
 *     <version>0.12.5</version>
 * </dependency>
 * <dependency>
 *     <groupId>io.jsonwebtoken</groupId>
 *     <artifactId>jjwt-impl</artifactId>
 *     <version>0.12.5</version>
 *     <scope>runtime</scope>
 * </dependency>
 * <dependency>
 *     <groupId>io.jsonwebtoken</groupId>
 *     <artifactId>jjwt-jackson</artifactId>
 *     <version>0.12.5</version>
 *     <scope>runtime</scope>
 * </dependency>
 * ```
 *
 * ## HOW TO RUN AND TEST THIS API:
 * 1.  Run the `main` method.
 * 2.  Use `curl` or an API client for a multi-step test.
 *
 *     - **Step 1: Log in to get a token.**
 *       `TOKEN=$(curl -s -X POST -H "Content-Type: application/json" -d '{"username":"user", "password":"password"}' http://localhost:8080/login | cut -d'"' -f4)`
 *       (This command logs in and saves the token to a shell variable named `TOKEN`).
 *       `echo $TOKEN` to view the token.
 *
 *     - **Step 2: Try accessing a protected endpoint WITHOUT the token.**
 *       `curl -i http://localhost:8080/api/secure`
 *       (This will fail with a `401 Unauthorized` or `403 Forbidden` error).
 *
 *     - **Step 3: Access the protected endpoint WITH the token.**
 *       `curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/secure`
 *       (This will succeed!)
 */

// --- Imports from Spring Security, JWT library, and other utilities ---
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

// --- Main Application Entry Point ---
@SpringBootApplication
public class JWT_AuthenticationAndAuthorization {
    public static void main(String[] args) {
        SpringApplication.run(JWT_AuthenticationAndAuthorization.class, args);
    }
}

// --- 1. Security Configuration ---
@Configuration
@EnableWebSecurity
class JwtSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless REST APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // Allow access to the login endpoint
                        .anyRequest().authenticated() // Secure all other endpoints
                )
                // Tell Spring Security not to manage sessions, as JWT is stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Add our custom JWT filter before the standard username/password filter
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean to configure an in-memory user store for demonstration purposes.
    @Bean
    public UserDetailsService userDetailsService() {
        var userDetails = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    // Bean for password hashing.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expose the AuthenticationManager as a Bean to be used in the AuthController.
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}

// --- 2. JWT Utility Class ---
@org.springframework.stereotype.Component
class JwtUtil {
    // A secure, randomly generated key. In a real app, this MUST be loaded from a
    // secure configuration source.
    private final SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private final long expiration = 1000 * 60 * 60; // 1 hour expiration

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}

// --- 3. Custom JWT Filter ---
// This filter runs once for every request to check for a valid JWT.
@org.springframework.stereotype.Component
class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtUtil ju, UserDetailsService uds) {
        this.jwtUtil = ju;
        this.userDetailsService = uds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication() == null) {
            var userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                var authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                org.springframework.security.core.context.SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response);
    }
}

// --- 4. Authentication Controller ---
@RestController
class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager am, JwtUtil ju) {
        this.authenticationManager = am;
        this.jwtUtil = ju;
    }

    record LoginRequest(String username, String password) {
    }

    record LoginResponse(String token) {
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        final String token = jwtUtil.generateToken(request.username());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}

// --- 5. A Secured Resource Controller ---
@RestController
class SecureController {
    @org.springframework.web.bind.annotation.GetMapping("/api/secure")
    public String getSecureData() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        return "This is secure data! You are authenticated as: " + auth.getName();
    }
}