import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import './App.css';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import AirportList from './AirportList';
import Login from './Login';
import Register from './Register';
import FlightList from './FlightList';
import RouteSearch from './RouteSearch';
import BuyTicket from './BuyTicket';
import Profile from './Profile';  // Importă componenta Profile
import Bookings from './Bookings';
import FavoriteFlights from './FavoriteFlights';
import Chat from './Chat';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [showRegister, setShowRegister] = useState(false);
    const [currentUser, setCurrentUser] = useState(null);
    const userEmail = localStorage.getItem("userEmail") || "";
    function connect() {
        const URL = "http://localhost:8080/socket";
        const socket = new SockJS(URL);
        const userEmail = localStorage.getItem("userEmail") || "";
        const stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
        });

        stompClient.onConnect = () => {
            stompClient.subscribe("/topic/socket/airports", notification => {
                let message = notification.body;
                console.log(message);
                alert(message);
            });
        };

        stompClient.activate();
    }
    useEffect(() => {
        connect();
    }, []);

    const handleLogout = () => {
        setIsLoggedIn(false);
        localStorage.removeItem("token");  // Înlătură token-ul din localStorage la logout
    };


    if (isLoggedIn) {
        return (
            <Router>
                <div className="App">
                    <h1>SkyGo</h1>

                    <nav>
                        <ul>
                            <li>
                                <Link to="/airports">Aeroporturi</Link>
                            </li>
                            <li>
                                <Link to="/flights">Zboruri</Link>
                            </li>
                            <li>
                                <Link to="/search-routes">Căutare Rute</Link>
                            </li>
                            <li>
                                <Link to="/profile">Profile</Link> {/* Link pentru pagina de profil */}
                            </li>
                            <li>
                                <Link to="/chat">Chat Public</Link>
                            </li>
                            <li>
                                <button onClick={handleLogout}>Logout</button>
                            </li>
                        </ul>
                    </nav>

                    <Routes>
                        <Route path="/airports" element={<AirportList />} />
                        <Route path="/flights" element={<FlightList currentUser={currentUser} onLogout={handleLogout} />} />
                        <Route path="/search-routes" element={<RouteSearch />} />
                        <Route path="/profile" element={<Profile currentUser={currentUser} />} />
                        <Route path="/" element={<h2>Pagina Principală</h2>} />
                        <Route path="/booking/purchase" element={<BuyTicket />} />
                        <Route path="/bookings" element={<Bookings />} />
                        <Route path="/favorites" element={<FavoriteFlights />} />
                        <Route path="/chat" element={<Chat />} />

                    </Routes>
                </div>
            </Router>
        );
    }

    return (
        <div className="App">
            <h1>SkyGo</h1>
            {showRegister ? (
                <Register
                    onRegisterSuccess={() => setShowRegister(false)}
                    onSwitchToLogin={() => setShowRegister(false)}
                />
            ) : (
                <Login
                    onLoginSuccess={(userData) => {
                        setCurrentUser(userData);
                        setIsLoggedIn(true);
                    }}
                />
            )}
            {!showRegister && (
                <p>Don't have an account? <button onClick={() => setShowRegister(true)}>Register</button></p>
            )}
        </div>
    );
}

export default App;
