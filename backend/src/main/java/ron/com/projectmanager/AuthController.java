package ron.com.projectmanager;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = JwtUtil.generateToken(username, user.getRole().name());

        return Map.of(
        "token", token,
        "role", user.getRole().name()
        );
    }


    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return Map.of("error", "Username and password are required");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return Map.of("error", "User already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);

        userRepository.save(user);

        return Map.of("message", "User registered successfully");
    }
}