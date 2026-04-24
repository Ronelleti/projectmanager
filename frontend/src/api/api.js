const API_URL = "http://ron.project.devops/api";

export function getToken() {
    return localStorage.getItem("token");
}

export function setToken(token) {
    localStorage.setItem("token", token);
}

export function isAuthenticated() {
    return !!getToken();
}

// LOGIN
export async function login(username, password) {
    const res = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    if (!res.ok) throw new Error("Login failed");

    const data = await res.json();
    setToken(data.token);
    return data;
}

// FETCH TASKS
export async function fetchTasks() {
    const res = await fetch(`${API_URL}/tasks`, {
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });

    if (!res.ok) throw new Error("Failed to fetch tasks");

    return res.json();
}

// DELETE TASK
export async function deleteTask(id) {
    const res = await fetch(`${API_URL}/tasks/${id}`, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });

    if (res.status === 403) {
        throw new Error("Not allowed (ADMIN only)");
    }

    if (!res.ok) throw new Error("Delete failed");
}