import React, { useEffect, useState } from "react";

function App() {
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState("");

  const API = "http://127.0.0.1:56540";

  const fetchTasks = async () => {
    const res = await fetch(`${API}/tasks`);
    const data = await res.json();
    setTasks(data);
  };

  useEffect(() => {
    fetchTasks();
  }, []);

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

  const deleteTask = async (id) => {
    await fetch(`${API}/tasks/${id}`, {
      method: "DELETE",
    });

    fetchTasks();
  };

  const completeTask = async (id) => {
    await fetch(`${API}/tasks/${id}/complete`, {
      method: "POST",
    });

    fetchTasks();
  };

  const assignTask = async (id) => {
    const user = prompt("Assign to:");
    if (!user) return;

    await fetch(`${API}/tasks/${id}/assign`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ assignedTo: user }),
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
                <b>{task.name}</b>
                {task.completed && " ✅"}
                {task.assignedTo && ` (👤 ${task.assignedTo})`}

                <br />

                <button onClick={() => completeTask(task.id)}>Complete</button>
                <button onClick={() => assignTask(task.id)}>Assign</button>
                <button onClick={() => deleteTask(task.id)}>❌</button>
              </li>
          ))}
        </ul>
      </div>
  );
}

export default App;