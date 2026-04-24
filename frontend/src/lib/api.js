const API = "http://ron.project.devops/api";

export function getToken() {
    return localStorage.getItem("token");
}

export function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    window.location.href = "/";
}

// 🔐 LOGIN
export async function login(username, password) {
    const res = await fetch(`${API}/auth/login`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ username, password })
    });

    if (!res.ok) throw new Error("Login failed");

    const data = await res.json();

    localStorage.setItem("token", data.token);
    localStorage.setItem("role", data.role);

    return data;
}

// 📥 TASKS
export async function fetchTasks() {
    const res = await fetch(`${API}/tasks`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });

    if (res.status === 401) logout();

    return res.json();
}

// ➕ ADD
export async function addTask(name) {
    const res = await fetch(`${API}/tasks`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${getToken()}`
        },
        body: JSON.stringify({ name })
    });

    if (res.status === 403) throw new Error("ADMIN only");
}

// ❌ DELETE
export async function deleteTask(id) {
    const res = await fetch(`${API}/tasks/${id}`, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });

    if (res.status === 403) throw new Error("ADMIN only");
}

// ✅ COMPLETE
export async function completeTask(id) {
    await fetch(`${API}/tasks/${id}/complete`, {
        method: "POST",
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });
}

// 👤 ASSIGN
export async function assignTask(id, user) {
    await fetch(`${API}/tasks/${id}/assign`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${getToken()}`
        },
        body: JSON.stringify({ assignedTo: user })
    });
}