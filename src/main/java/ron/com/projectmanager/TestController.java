package ron.com.projectmanager;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TestController {

    private List<Task> tasks = new ArrayList<>();
    private int currentId = 1;

    // GET all tasks
    @GetMapping
    public List<Task> getTasks() {
        return tasks;
    }

    // CREATE task
    @PostMapping
    public Task addTask(@RequestBody Task task) {
        task.setId(currentId++);
        tasks.add(task);
        return task;
    }

    // DELETE task
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable int id) {
        tasks.removeIf(t -> t.getId() == id);
        return "Task deleted";
    }

    // UPDATE task name
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Optional<Task> taskOpt = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst();

        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setName(updatedTask.getName());
            return task;
        }

        throw new RuntimeException("Task not found");
    }

    // ASSIGN task
    @PostMapping("/{id}/assign")
    public Task assignTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Optional<Task> taskOpt = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst();

        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setAssignedTo(updatedTask.getAssignedTo());
            return task;
        }

        throw new RuntimeException("Task not found");
    }

    // MARK COMPLETE
    @PostMapping("/{id}/complete")
    public Task completeTask(@PathVariable int id) {
        Optional<Task> taskOpt = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst();

        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setCompleted(true);
            return task;
        }

        throw new RuntimeException("Task not found");
    }
}