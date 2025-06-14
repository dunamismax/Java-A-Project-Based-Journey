
/**
 * This lesson explores how to model and query relationships between database
 * entities using JPA, a fundamental skill for building complex applications.
 *
 * Real-world data is rarely isolated. A customer has many orders; an order has many
 * line items. We will model a classic **`User` -> `Post`** relationship.
 *
 * ## Core Concepts & Annotations Covered:
 *
 * - **JPA Relationships**:
 *   - **`@ManyToOne`**: Used on the "many" side of a relationship. Many `Post` entities
 *     can belong to one `User`. This is the side that typically owns the relationship
 *     and holds the foreign key column (`user_id`).
 *   - **`@OneToMany`**: Used on the "one" side. One `User` can have many `Post` entities.
 *     The `mappedBy` attribute tells JPA that the other side (`Post.user`) manages this
 *     relationship.
 *   - **`@JoinColumn`**: Specifies the foreign key column in the database table.
 *
 * - **Bidirectional vs. Unidirectional**: Our relationship will be bidirectional, meaning
 *   you can navigate from a `User` to their `Posts`, and from a `Post` back to its `User`.
 *
 * - **Custom Queries**: We'll write a custom Spring Data JPA query to fetch a user and
 *   all their posts efficiently, avoiding a common performance pitfall known as the
 *   "N+1 problem."
 *
 * ## PREQUISITES:
 * The same `spring-boot-starter-data-jpa` and `h2` dependencies, and the same
 * `application.properties` settings as the previous lessons.
 *
 * ## HOW TO RUN AND TEST THIS API:
 * 1.  Run the `main` method. Observe the console as Hibernate creates two tables,
 *     `app_user` and `post`, with a foreign key relationship.
 * 2.  Use `curl` to test the endpoints:
 *
 *     - **Get all users (without posts):** `curl http://localhost:8080/api/users`
 *
 *     - **Get all posts (includes user info):** `curl http://localhost:8080/api/posts`
 *
 *     - **Get a specific user WITH their posts (our custom query):**
 *       `curl http://localhost:8080/api/users/1/with-posts`
 */

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// --- Main Application Entry Point ---
@SpringBootApplication
public class RelationshipsAndQueries {
    public static void main(String[] args) {
        SpringApplication.run(RelationshipsAndQueries.class, args);
    }

    // Use a CommandLineRunner to create some users and posts on startup.
    @Bean
    CommandLineRunner runner(AppUserRepository userRepo, PostRepository postRepo) {
        return args -> {
            AppUser user1 = userRepo.save(new AppUser("Alice"));
            AppUser user2 = userRepo.save(new AppUser("Bob"));

            postRepo.save(new Post("My first post!", user1));
            postRepo.save(new Post("A lovely day for JPA", user1));
            postRepo.save(new Post("Getting started with Spring", user2));
        };
    }
}

// --- 1. The Entities with Relationships ---

// We name the class `AppUser` to avoid conflicts with the `User` class from
// other lessons.
// We also name the table `app_user` because `user` is often a reserved keyword
// in SQL.
@Entity
@Table(name = "app_user")
class AppUser {
    @Id
    @GeneratedValue
    private Long id;
    private String username;

    // A User can have many Posts.
    // `mappedBy = "user"` tells JPA that the `user` field in the `Post` class owns
    // the relationship.
    // `cascade = CascadeType.ALL` means if we delete a user, all their posts are
    // also deleted.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Handles circular references during JSON serialization.
    private List<Post> posts = new ArrayList<>();

    protected AppUser() {
    }

    public AppUser(String u) {
        this.username = u;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<Post> getPosts() {
        return posts;
    }
}

@Entity
class Post {
    @Id
    @GeneratedValue
    private Long id;
    private String content;

    // Many Posts can belong to one User.
    // This is the "owning" side of the relationship. JPA will create a `user_id`
    // foreign key column in the `post` table.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference // The "back" part of the reference to prevent infinite loops in JSON.
    private AppUser user;

    protected Post() {
    }

    public Post(String c, AppUser u) {
        this.content = c;
        this.user = u;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public AppUser getUser() {
        return user;
    }
}

// --- 2. The Repositories with Custom Queries ---

@Repository
interface AppUserRepository extends JpaRepository<AppUser, Long> {
    // This custom query uses `LEFT JOIN FETCH` to eagerly load a user AND all their
    // associated posts in a single, efficient database query. This solves the "N+1
    // select problem."
    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.posts WHERE u.id = :id")
    Optional<AppUser> findByIdWithPosts(@Param("id") Long id);
}

@Repository
interface PostRepository extends JpaRepository<Post, Long> {
}

// --- 3. The Web API Controllers ---

@RestController
@RequestMapping("/api/users")
class AppUserController {
    private final AppUserRepository userRepository;

    public AppUserController(AppUserRepository r) {
        this.userRepository = r;
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        // Note: By default, this will NOT include the posts due to lazy loading.
        return userRepository.findAll();
    }

    @GetMapping("/{id}/with-posts")
    public ResponseEntity<AppUser> getUserWithPosts(@PathVariable Long id) {
        // Here we call our custom, efficient query.
        return userRepository.findByIdWithPosts(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

@RestController
@RequestMapping("/api/posts")
class PostController {
    private final PostRepository postRepository;

    public PostController(PostRepository r) {
        this.postRepository = r;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        // This will include basic user info for each post.
        return postRepository.findAll();
    }
}