package ron.com.projectmanager;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RestController
public class TestController {

    private List<Task> tasks = new ArrayList<>();
    private int currentId = 1;

    // ✅ ROOT endpoint (so "/" works)
    @GetMapping("/")
    public String home() {
        return "Project Manager API is running 🚀";
    }

    // GET all tasks
    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return tasks;
    }

    //Debug!!!
    @PostMapping("/debug")
    public String debug(@RequestBody String body) {
        return body;
    }

    // GET single task
    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable int id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // CREATE task
    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task) {
        task.setId(currentId++);
        tasks.add(task);
        return task;
    }

    // DELETE task
    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable int id) {
        boolean removed = tasks.removeIf(t -> t.getId() == id);

        if (!removed) {
            throw new RuntimeException("Task not found");
        }

        return "Task deleted";
    }

    // UPDATE task name
    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Task task = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setName(updatedTask.getName());
        return task;
    }

    // ASSIGN task
    @PostMapping("/tasks/{id}/assign")
    public Task assignTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Task task = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setAssignedTo(updatedTask.getAssignedTo());
        return task;
    }

    // MARK COMPLETE
    @PostMapping("/tasks/{id}/complete")
    public Task completeTask(@PathVariable int id) {
        Task task = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setCompleted(true);
        return task;
    }
}