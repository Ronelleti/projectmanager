package ron.com.projectmanager;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TestController {

    private final TaskRepository taskRepository;

    // 🔥 Inject repository
    public TestController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // ROOT
    @GetMapping
    public String home() {
        return "Project Manager API is running 🚀";
    }

    // GET all tasks
    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    // GET one task
    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // CREATE
    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    // DELETE
    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable int id) {
        taskRepository.deleteById(id);
        return "Task deleted";
    }

    // UPDATE
    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setName(updatedTask.getName());
        return taskRepository.save(task);
    }

    // ASSIGN
    @PostMapping("/tasks/{id}/assign")
    public Task assignTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setAssignedTo(updatedTask.getAssignedTo());
        return taskRepository.save(task);
    }

    // COMPLETE
    @PostMapping("/tasks/{id}/complete")
    public Task completeTask(@PathVariable int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setCompleted(true);
        return taskRepository.save(task);
    }
}