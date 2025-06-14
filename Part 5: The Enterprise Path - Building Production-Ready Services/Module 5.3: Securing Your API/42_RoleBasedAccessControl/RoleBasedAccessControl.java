
/**
 * This lesson builds on our JWT authentication system to add **Authorization**,
 * specifically **Role-Based Access Control (RBAC)**.
 *
 * Authentication answers "Who are you?". Authorization answers "What are you allowed to do?".
 *
 * With RBAC, we assign roles to users (e.g., `ROLE_USER`, `ROLE_ADMIN`) and then
 * secure specific endpoints or methods so that only users with the required role
 * can access them.
 *
 * ## Core Concepts & Annotations Covered:
 *
 * - **Roles vs. Authorities**: In Spring Security, roles are a type of "authority"
 *   and are typically prefixed with `ROLE_`.
 * - **`@EnableMethodSecurity`**: A crucial annotation that enables method-level security checks.
 * - **`@PreAuthorize`**: A powerful annotation used on controller methods to enforce
 *   authorization rules *before* the method is executed. We will use expressions like
 *   `hasRole('ADMIN')` or `hasAnyRole('USER', 'ADMIN')`.
 * - **Storing Roles in the JWT**: We will modify our `JwtUtil` to include the user's
 *   authorities (roles) as a custom "claim" within the JWT payload.
 * - **Reading Roles from the JWT**: Our `JwtRequestFilter` will be updated to parse
 *   these roles from the token and create a fully populated `Authentication` object.
 *
 * ## PREQUISITES: The same as the previous JWT lesson.
 * This file directly refactors and extends the code from `41_JWT_AuthenticationAndAuthorization.java`.
 *
 * ## HOW TO RUN AND TEST THIS API:
 * 1.  Run the `main` method.
 * 2.  Use `curl` or an API client for a multi-step test, logging in as different users.
 *
 *     - **Step 1: Get a token for a regular user.**
 *       `USER_TOKEN=$(curl -s -X POST -H "Content-Type: application/json" -d '{"username":"user", "password":"password"}' http://localhost:8080/login | cut -d'"' -f4)`
 *
 *     - **Step 2: Get a token for an admin.**
 *       `ADMIN_TOKEN=$(curl -s -X POST -H "Content-Type: application/json" -d '{"username":"admin", "password":"password"}' http://localhost:8080/login | cut -d'"' -f4)`
 *
 *     - **Step 3: Test the endpoints.**
 *       `curl -H "Authorization: Bearer $USER_TOKEN" http://localhost:8080/api/data/user` (✅ Succeeds)
 *       `curl -i -H "Authorization: Bearer $USER_TOKEN" http://localhost:8080/api/data/admin` (❌ Fails with 403 Forbidden)
 *       `curl -H "Authorization: Bearer $ADMIN_TOKEN" http://localhost:8080/api/data/user` (✅ Succeeds)
 *       `curl -H "Authorization: Bearer $ADMIN_TOKEN" http://localhost:8080/api/data/admin` (✅ Succeeds)
 */

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// --- Main Application Entry Point ---
@SpringBootApplication
public class RoleBasedAccessControl {
    public static void main(String[] args) {
        SpringApplication.run(RoleBasedAccessControl.class, args);
    }
}

// --- 1. Security Configuration (with Method Security Enabled) ---
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // This annotation is crucial! It enables @PreAuthorize.
class RbacSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter filter) throws Exception {
        http.csrf(c -> c.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // We now define two users with different roles.
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER") // ROLE_USER
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN", "USER") // ROLE_ADMIN and ROLE_USER
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}

// --- 2. JWT Utility (Updated to handle roles) ---
@Component
class JwtUtil {
    private final SecretKey secretKey = io.jsonwebtoken.security.Keys
            .secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    public String generateToken(UserDetails userDetails) {
        // Collect roles from UserDetails and add them as a custom "roles" claim.
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles) // Add roles claim
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(secretKey)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }
}

// --- 3. JWT Filter (Updated to read roles) ---
@Component
class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil util) {
        this.jwtUtil = util;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws java.io.IOException, jakarta.servlet.ServletException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractUsername(jwt);
            if (username != null && org.springframework.security.core.context.SecurityContextHolder.getContext()
                    .getAuthentication() == null) {
                // Read roles from the token and create GrantedAuthority objects.
                List<String> roles = jwtUtil.extractRoles(jwt);
                Collection<? extends GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Create the authentication token with the extracted roles.
                var authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                org.springframework.security.core.context.SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }
        chain.doFilter(req, res);
    }
}

// --- 4. Authentication Controller (Updated to generate token with roles) ---
@RestController
class AuthController {
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager am, UserDetailsService uds, JwtUtil ju) {
        this.authManager = am;
        this.userDetailsService = uds;
        this.jwtUtil = ju;
    }

    record LoginRequest(String username, String password) {
    }

    record LoginResponse(String token) {
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        // Fetch UserDetails to get the roles for token generation.
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        final String token = jwtUtil.generateToken(userDetails);
        return new LoginResponse(token);
    }
}

// --- 5. Secured Resource Controller with Role Checks ---
@RestController
@RequestMapping("/api/data")
class DataController {
    // This endpoint can be accessed by anyone with the 'USER' role.
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String getUserData() {
        return "This is data accessible to all authenticated USERS.";
    }

    // This endpoint can ONLY be accessed by users with the 'ADMIN' role.
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminData() {
        return "This is sensitive data, accessible only by ADMINS.";
    }
}