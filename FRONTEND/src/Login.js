// src/Login.js
import React, { useState } from 'react';

function Login({ onLoginSuccess }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (event) => {
        event.preventDefault();

        try {
            const response = await fetch("http://localhost:8082/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: username, // trebuie să se potrivească cu AuthDTO din backend
                    password: password,
                }),
            });

            if (response.ok) {
                const userProfileResp = await fetch(`http://localhost:8080/user/profile?email=${username}`);
                if (userProfileResp.ok) {
                    const userData = await userProfileResp.json();
                    sessionStorage.setItem("userEmail", username);
                    onLoginSuccess(userData);
                    alert(`Bun venit, ${username}!`);
                } else {
                    alert("Nu s-a putut obține profilul utilizatorului!");
                }
            }
            else {
                const error = await response.text();
                alert("Login failed: " + error);
            }
        } catch (err) {
            alert("Error: " + err.message);
        }
    };

    return (
        <div className="login-container">
            <h1>Login - SkyGo</h1>
            <form onSubmit={handleLogin}>
                <div>
                    <label htmlFor="username">Email</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Login</button>
            </form>
        </div>
    );
}

export default Login;
