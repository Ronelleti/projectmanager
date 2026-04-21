import React, { useEffect, useState } from "react";

function App() {
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState("");

  const API = "http://127.0.0.1:56540"; // your backend URL

  // Fetch tasks
  const fetchTasks = async () => {
    const res = await fetch(`${API}/tasks`);
    const data = await res.json();
    setTasks(data);
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  // Add task
  const addTask = async () => {
    if (!newTask) return;

    await fetch(`${API}/tasks`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name: newTask }),
    });

    setNewTask("");
    fetchTasks();
  };

  // Delete task
  const deleteTask = async (id) => {
    await fetch(`${API}/tasks/${id}`, {
      method: "DELETE",
    });

    fetchTasks();
  };

  return (
      <div style={{ padding: "20px" }}>
        <h1>Project Manager 🚀</h1>

        <input
            value={newTask}
            onChange={(e) => setNewTask(e.target.value)}
            placeholder="New task"
        />
        <button onClick={addTask}>Add</button>

        <ul>
          {tasks.map((task) => (
              <li key={task.id}>
                {task.name}
                <button onClick={() => deleteTask(task.id)}>❌</button>
              </li>
          ))}
        </ul>
      </div>
  );
}

export default App;