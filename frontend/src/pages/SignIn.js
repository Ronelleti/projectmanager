import React, { useEffect, useState } from "react";
import LoginPage from "../components/project/LoginPage";
import { login } from "../lib/api";

export default function SignIn() {
    const [token] = useState(localStorage.getItem("token"));

    useEffect(() => {
        if (token) window.location.href = "/dashboard";
    }, [token]);

    const handleLogin = async (username, password) => {
        try {
            await login(username, password);
            window.location.href = "/dashboard";
        } catch {
            alert("Login failed");
        }
    };

    return <LoginPage onLogin={handleLogin} />;
}