package ron.com.projectmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjectmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectmanagerApplication.class, args);
    }

    // 🔥 TEMP: create admin user on startup
    @Bean
    CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {

                User user = new User();
                user.setUsername("admin");
                user.setPassword(encoder.encode("admin")); // 🔐 hashed password
                user.setRole(Role.ADMIN);

                repo.save(user);

                System.out.println("🔥 Admin user created");
            }
        };
    }
}