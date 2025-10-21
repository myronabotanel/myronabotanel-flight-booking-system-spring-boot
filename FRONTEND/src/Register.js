import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

function Register({ onRegisterSuccess, onSwitchToLogin }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [emailError, setEmailError] = useState('');
    const [passwordError, setPasswordError] = useState('');

    const handleRegister = async (event) => {
        event.preventDefault();

        // Reset previous errors
        setEmailError('');
        setPasswordError('');

        let valid = true;

        // Frontend validation logic
        if (!email) {
            setEmailError('Email is required');
            valid = false;
        } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(email)) {
            setEmailError('Invalid email address');
            valid = false;
        }

        if (!password) {
            setPasswordError('Password is required');
            valid = false;
        } else {
            const hasUpperCase = /[A-Z]/.test(password);
            const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
            const hasNumber = /[0-9]/.test(password);
            const hasMinLength = password.length >= 8;

            if (!hasMinLength) {
                setPasswordError('Password must be at least 8 characters long');
                valid = false;
            } else if (!hasUpperCase) {
                setPasswordError('Password must contain at least one uppercase letter');
                valid = false;
            } else if (!hasSpecialChar) {
                setPasswordError('Password must contain at least one special character');
                valid = false;
            } else if (!hasNumber) {
                setPasswordError('Password must contain at least one number');
                valid = false;
            }
        }

        if (!valid) {
            return; // Stop if validation fails
        }

        try {
            // Trimite cererea de înregistrare doar la backend-ul principal (8080)
            const response = await fetch("http://localhost:8080/user/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: email,
                    password: password,
                }),
            });

            if (!response.ok) {
                const error = await response.text();
                alert("Înregistrare eșuată: " + error);
                return;
            }

            alert("Cont creat cu succes!");
            onRegisterSuccess();
        } catch (err) {
            alert("Eroare la înregistrare: " + err.message);
        }
    };
    

    return (
        <div className="register-container">
            <h1>Register - SkyGo</h1>
            <form onSubmit={handleRegister}>
                <div>
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    {emailError && <div style={{ color: 'red' }}>{emailError}</div>}
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    {passwordError && <div style={{ color: 'red' }}>{passwordError}</div>}
                </div>
                <button type="submit">Register</button>
            </form>
            <p>Already have an account? <button onClick={onSwitchToLogin}>Login</button></p>
        </div>
    );
}

export default Register;