import { useState } from "react";

export default function LoginPage({ onLogin }) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-900 to-black flex items-center justify-center">

            <div className="w-full max-w-sm bg-slate-900/60 backdrop-blur-xl border border-slate-700 rounded-2xl p-8 shadow-2xl">

                {/* Title */}
                <h1 className="text-2xl font-semibold text-white mb-6 text-center">
                    🔐 Login
                </h1>

                {/* Username */}
                <input
                    className="w-full mb-4 p-3 rounded-lg bg-slate-800 text-white outline-none"
                    placeholder="Username"
                    onChange={(e) => setUsername(e.target.value)}
                    onKeyDown={(e) => {
                        if (e.key === "Enter") onLogin(username, password);
                    }}
                />

                {/* Password */}
                <input
                    type="password"
                    className="w-full mb-6 p-3 rounded-lg bg-slate-800 text-white outline-none"
                    placeholder="Password"
                    onChange={(e) => setPassword(e.target.value)}
                    onKeyDown={(e) => {
                        if (e.key === "Enter") onLogin(username, password);
                    }}
                />

                {/* Button */}
                <button
                    onClick={() => onLogin(username, password)}
                    className="w-full bg-blue-500 hover:bg-blue-600 transition p-3 rounded-lg text-white font-medium"
                >
                    Sign In
                </button>

            </div>
        </div>
    );
}