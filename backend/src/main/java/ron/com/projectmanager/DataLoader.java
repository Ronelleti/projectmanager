package ron.com.projectmanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadUsers(UserRepository repo) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                User user = new User();
                user.setUsername("admin");
                user.setPassword(encoder.encode("admin"));
                user.setRole(Role.ADMIN);

                repo.save(user);
            }
        };
    }
}