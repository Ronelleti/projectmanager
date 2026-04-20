package ron.com.projectmanager;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {

    private List<String> tasks = new ArrayList<>();

    // GET all tasks
    @GetMapping("/tasks")
    public List<String> getTasks() {
        return tasks;
    }

    // ADD new task
    @PostMapping("/tasks")
    public String addTask(@RequestBody String task) {
        tasks.add(task);
        return "Task added!";
    }

    // DELETE task by index
    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable int id) {
        if (id >= 0 && id < tasks.size()) {
            tasks.remove(id);
            return "Task deleted!";
        } else {
            return "Invalid task id!";
        }
    }
}