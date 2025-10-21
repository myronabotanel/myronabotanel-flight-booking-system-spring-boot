import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function Profile({ currentUser }) {
    const [user, setUser] = useState(null);
    const [favoriteFlights, setFavoriteFlights] = useState([]);

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const email = currentUser?.email || sessionStorage.getItem("userEmail");
                if (!email) {
                    alert("No user logged in");
                    return;
                }

                const response = await fetch(`http://localhost:8080/user/profile?email=${email}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const data = await response.json();
                    setUser(data);
                    // După ce avem datele utilizatorului, încărcăm zborurile favorite
                    if (data.id) {
                        fetchFavoriteFlights(data.id);
                    }
                } else {
                    alert("Failed to load user data");
                }
            } catch (error) {
                console.error("Error fetching user profile:", error);
            }
        };

        fetchUserProfile();
    }, [currentUser]);

    const fetchFavoriteFlights = async (userId) => {
        try {
            const response = await fetch(`http://localhost:8080/user/${userId}/favorites`);
            if (response.ok) {
                const data = await response.json();
                setFavoriteFlights(data);
            } else {
                console.error("Failed to load favorite flights");
            }
        } catch (error) {
            console.error("Error fetching favorite flights:", error);
        }
    };

    const removeFavoriteFlight = async (flightId) => {
        try {
            const response = await fetch(`http://localhost:8080/user/${user.id}/favorites/${flightId}`, {
                method: 'DELETE',
            });
            if (response.ok) {
                // Reîmprospătează lista de zboruri favorite
                fetchFavoriteFlights(user.id);
            } else {
                alert("Failed to remove flight from favorites");
            }
        } catch (error) {
            console.error("Error removing favorite flight:", error);
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <h1>Profil utilizator</h1>
            {user ? (
                <div>
                    <div style={{ marginBottom: '30px' }}>
                        <h2>Informații personale</h2>
                        {Object.entries(user).map(([key, value]) => {
                            // Exclude favoriteFlights din afișarea informațiilor personale
                            if (key === 'favoriteFlights') return null;
                            return (
                                <p key={key}>
                                    {key.charAt(0).toUpperCase() + key.slice(1)}:{" "}
                                    {Array.isArray(value) ? value.length : value?.toString()}
                                </p>
                            );
                        })}
                    </div>

                    <div style={{ marginBottom: '30px' }}>
                        <h2>Zboruri favorite</h2>
                        {favoriteFlights.length > 0 ? (
                            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '20px' }}>
                                {favoriteFlights.map((flight) => (
                                    <div
                                        key={flight.id}
                                        style={{
                                            padding: '15px',
                                            border: '1px solid #ddd',
                                            borderRadius: '8px',
                                            position: 'relative'
                                        }}
                                    >
                                        <h3>{flight.airline}</h3>
                                        <p>{flight.departureAirport?.city} → {flight.arrivalAirport?.city}</p>
                                        <p>Plecare: {new Date(flight.departureTime).toLocaleString()}</p>
                                        <p>Sosire: {new Date(flight.arrivalTime).toLocaleString()}</p>
                                        <p>Preț: {flight.pricePerSeat} RON</p>
                                        <button
                                            onClick={() => removeFavoriteFlight(flight.id)}
                                            style={{
                                                padding: '5px 10px',
                                                backgroundColor: '#ff4444',
                                                color: 'white',
                                                border: 'none',
                                                borderRadius: '4px',
                                                cursor: 'pointer',
                                                position: 'absolute',
                                                top: '10px',
                                                right: '10px'
                                            }}
                                        >
                                            Șterge din favorite
                                        </button>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <p>Nu ai niciun zbor favorit.</p>
                        )}
                    </div>

                    <div style={{ marginTop: '20px' }}>
                        <Link 
                            to="/bookings"
                            style={{
                                padding: '10px 20px',
                                backgroundColor: '#4CAF50',
                                color: 'white',
                                textDecoration: 'none',
                                borderRadius: '5px',
                                display: 'inline-block'
                            }}
                        >
                            Vezi rezervările
                        </Link>
                    </div>
                </div>
            ) : (
                <p>Se încarcă profilul...</p>
            )}
        </div>
    );
}

export default Profile;