package ron.com.projectmanager;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TestController {

    private final TaskRepository taskRepository;

    public TestController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // ROOT
    @GetMapping
    public String home() {
        return "Project Manager API is running 🚀";
    }

    // 🟢 READ (USER + ADMIN)
    @GetMapping("/tasks")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/tasks/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Task getTask(@PathVariable int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // 🔴 CREATE (ADMIN ONLY)
    @PostMapping("/tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public Task addTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    // 🔴 DELETE (ADMIN ONLY)
    @DeleteMapping("/tasks/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTask(@PathVariable int id) {
        taskRepository.deleteById(id);
        return "Task deleted";
    }

    // 🔴 UPDATE (ADMIN ONLY)
    @PutMapping("/tasks/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Task updateTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setName(updatedTask.getName());
        return taskRepository.save(task);
    }

    // 🔴 ASSIGN (ADMIN ONLY)
    @PostMapping("/tasks/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public Task assignTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setAssignedTo(updatedTask.getAssignedTo());
        return taskRepository.save(task);
    }

    // 🔴 COMPLETE (ADMIN ONLY)
    @PostMapping("/tasks/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public Task completeTask(@PathVariable int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setCompleted(true);
        return taskRepository.save(task);
    }
}