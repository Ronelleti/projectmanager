import React, { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Inbox } from "lucide-react";

// ✅ FIXED PATHS (NO @)
import TaskHeader from "../components/project/TaskHeader";
import AddTaskBar from "../components/project/AddTaskBar";
import TaskCard from "../components/project/TaskCard";
import TaskStats from "../components/project/TaskStats";

// ✅ FIXED PATH
import {
    fetchTasks,
    addTask,
    deleteTask,
    completeTask,
    assignTask,
    logout
} from "../lib/api";

export default function Dashboard() {
    const [tasks, setTasks] = useState([]);
    const token = localStorage.getItem("token");
    const [role] = useState(localStorage.getItem("role"));
    const [newTask, setNewTask] = useState("");

    useEffect(() => {
        if (!token) window.location.href = "/";
    }, [token]);

    const loadTasks = async () => {
        try {
            const data = await fetchTasks();
            setTasks(data);
        } catch {
            alert("Failed to load tasks");
        }
    };

    useEffect(() => {
        if (token) loadTasks();
    }, [token]);

    const handleAdd = async (name) => {
        try {
            await addTask(name);
            loadTasks();
        } catch {
            alert("Only ADMIN can add tasks");
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteTask(id);
            loadTasks();
        } catch {
            alert("Only ADMIN can delete");
        }
    };

    const handleComplete = async (id) => {
        await completeTask(id);
        loadTasks();
    };

    const handleAssign = async (id) => {
        const user = prompt("Assign to:");
        if (!user) return;

        await assignTask(id, user);
        loadTasks();
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-900 to-black flex items-center justify-center">
            <div className="w-full max-w-3xl bg-slate-900/60 backdrop-blur-xl border border-slate-700 rounded-2xl p-8 shadow-2xl">

                {/* Header */}
                <div className="flex justify-between items-center mb-6">
                    <h1 className="text-xl font-semibold text-white">🚀 Project Manager</h1>
                    <button
                        onClick={logout}
                        className="text-sm text-slate-400 hover:text-white"
                    >
                        Sign out
                    </button>
                </div>

                {/* Stats */}
                <div className="grid grid-cols-3 gap-4 mb-8">
                    <div className="bg-slate-800 p-4 rounded-xl text-center">
                        <p className="text-white text-lg font-bold">{tasks.length}</p>
                        <p className="text-slate-400 text-sm">Total</p>
                    </div>

                    <div className="bg-slate-800 p-4 rounded-xl text-center">
                        <p className="text-green-400 text-lg font-bold">
                            {tasks.filter(t => t.completed).length}
                        </p>
                        <p className="text-slate-400 text-sm">Completed</p>
                    </div>

                    <div className="bg-slate-800 p-4 rounded-xl text-center">
                        <p className="text-yellow-400 text-lg font-bold">
                            {tasks.filter(t => !t.completed).length}
                        </p>
                        <p className="text-slate-400 text-sm">Pending</p>
                    </div>
                </div>

                {/* Add Task */}
                {role === "ADMIN" && (
                    <div className="flex gap-2 mb-6">
                        <input
                            className="flex-1 bg-slate-800 text-white p-2 rounded-lg outline-none"
                            placeholder="Add new task..."
                            onChange={(e) => setNewTask(e.target.value)}
                        />
                        <button
                            onClick={async () => {
                                await addTask(newTask);
                                setNewTask("");   // clear input
                                loadTasks();      // reload tasks
                            }}
                            className="bg-blue-500 px-4 rounded-lg text-white"
                        >
                            Add
                        </button>
                    </div>
                )}

                {/* Tasks */}
                <div className="space-y-2">
                    {tasks.length === 0 ? (
                        <div className="text-center text-slate-500 py-10">
                            No tasks yet
                        </div>
                    ) : (
                        tasks.map(task => (
                            <div
                                key={task.id}
                                className="bg-slate-800 p-3 rounded-lg flex justify-between items-center"
                            >
                                <div>
                                    {task.name}

                                    {task.completed && (
                                        <span className="ml-2 text-green-400">✔</span>
                                    )}

                                    {task.assignedTo && (
                                        <span className="ml-2 text-blue-400">
                                            👤 {task.assignedTo}
                                        </span>
                                    )}
                                </div>

                                <div className="flex gap-2">
                                    {/* COMPLETE */}
                                    <button
                                        onClick={async () => {
                                            try {
                                                await completeTask(task.id);
                                                loadTasks();
                                            } catch (e) {
                                                alert("Failed to complete task");
                                            }
                                        }}
                                    >
                                        ✔
                                    </button>

                                    {/* ASSIGN */}
                                    <button
                                        onClick={async () => {
                                            const user = prompt("Assign to:");
                                            if (!user) return;

                                            try {
                                                await assignTask(task.id, user);
                                                loadTasks();
                                            } catch (e) {
                                                alert("Failed to assign task");
                                            }
                                        }}
                                    >
                                        👤
                                    </button>

                                    {/* DELETE (ADMIN ONLY) */}
                                    {role === "ADMIN" && (
                                        <button
                                            onClick={async () => {
                                                try {
                                                    await deleteTask(task.id);
                                                    loadTasks();
                                                } catch (e) {
                                                    alert(e.message || "Delete failed");
                                                }
                                            }}
                                        >
                                            ❌
                                        </button>
                                    )}
                                </div>
                            </div>
                        ))
                    )}
                </div>

            </div>
        </div>
    );
}