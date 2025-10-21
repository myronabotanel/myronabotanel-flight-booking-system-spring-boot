import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function FavoriteFlights() {
    const [favoriteFlights, setFavoriteFlights] = useState([]);
    const [currentUser, setCurrentUser] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const email = localStorage.getItem("userEmail"); // Folosește "userEmail" aici
        if (!email) {
            navigate('/');
            return;
        }

        // Obține detaliile utilizatorului
        fetch(`http://localhost:8080/user/profile?email=${email}`)
            .then(response => response.json())
            .then(userData => {
                setCurrentUser(userData);
                // Obține zborurile favorite folosind endpoint-ul corect
                return fetch(`http://localhost:8080/user/${userData.id}/favorites`);
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch favorites');
                }
                return response.json();
            })
            .then(data => {
                setFavoriteFlights(data);
            })
            .catch(error => {
                console.error("Eroare la încărcarea favoritelelor:", error);
            });
    }, [navigate]);

    const removeFromFavorites = (flightId) => {
        fetch(`http://localhost:8080/user/${currentUser.id}/favorites/${flightId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    setFavoriteFlights(prev => prev.filter(f => f.id !== flightId));
                }
            })
            .catch(error => console.error("Eroare la ștergerea favoritei:", error));
    };

    return (
        <div style={{ padding: '20px' }}>
            <h1>Zboruri Favorite</h1>
            {favoriteFlights.length > 0 ? (
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                    <tr>
                        <th>Companie Aeriană</th>
                        <th>Plecare din</th>
                        <th>Destinație</th>
                        <th>Data Plecare</th>
                        <th>Data Sosire</th>
                        <th>Preț</th>
                        <th>Acțiuni</th>
                    </tr>
                    </thead>
                    <tbody>
                    {favoriteFlights.map((flight) => (
                        <tr key={flight.id}>
                            <td>{flight.airline}</td>
                            <td>{flight.departureAirport?.name || "N/A"}</td>
                            <td>{flight.arrivalAirport?.name || "N/A"}</td>
                            <td>{new Date(flight.departureTime).toLocaleString()}</td>
                            <td>{new Date(flight.arrivalTime).toLocaleString()}</td>
                            <td>{flight.pricePerSeat} €</td>
                            <td>
                                <button
                                    onClick={() => removeFromFavorites(flight.id)}
                                    style={{
                                        background: 'none',
                                        border: 'none',
                                        color: 'red',
                                        cursor: 'pointer',
                                        fontSize: '1.2em'
                                    }}
                                >
                                    ❌
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <p>Nu ai zboruri favorite.</p>
            )}
        </div>
    );
}

export default FavoriteFlights;