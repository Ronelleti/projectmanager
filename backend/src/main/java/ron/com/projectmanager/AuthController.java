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

    // 🔐 LOGIN
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
        String refreshToken = JwtUtil.generateRefreshToken(username);

        return Map.of(
                "token", token,
                "refreshToken", refreshToken,
                "role", user.getRole().name(),
                "mustChangePassword", String.valueOf(user.isMustChangePassword())
        );
    }

    // 🔄 REFRESH TOKEN (NEW)
    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        try {
            String username = JwtUtil.extractUsername(refreshToken);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String newToken = JwtUtil.generateToken(username, user.getRole().name());

            return Map.of("token", newToken);

        } catch (Exception e) {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    // 🆕 REGISTER
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return Map.of("error", "Username and password are required");
        }

        if (!isValidPassword(password)) {
            return Map.of("error", "Password must be at least 5 characters and contain letters and numbers");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return Map.of("error", "User already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);
        user.setMustChangePassword(true);

        userRepository.save(user);

        return Map.of("message", "User registered successfully");
    }

    // 🔐 CHANGE PASSWORD
    @PostMapping("/change-password")
    public Map<String, String> changePassword(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String newPassword = request.get("newPassword");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!isValidPassword(newPassword)) {
            return Map.of("error", "Password must be at least 5 characters and contain letters and numbers");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);

        userRepository.save(user);

        return Map.of("message", "Password updated");
    }

    // 🔐 PASSWORD VALIDATION
    private boolean isValidPassword(String password) {
        return password.length() >= 5
                && password.matches(".*[A-Za-z].*")
                && password.matches(".*\\d.*");
    }
}