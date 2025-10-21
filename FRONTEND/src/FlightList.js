import React, { useEffect, useState } from 'react';

// Stiluri pentru butonul de inimă
const heartButtonStyle = {
    background: 'none',
    border: 'none',
    fontSize: '1.5em',
    cursor: 'pointer',
    padding: '5px',
    color: '#ff4d4d',
    transition: 'transform 0.2s ease'
};

// Componentă pentru afișarea și gestionarea zborurilor
function FlightList({ onLogout, currentUser }) {
    // Stare pentru zboruri
    const [flights, setFlights] = useState([]);
    // Stare pentru lista de aeroporturi (folosită în dropdown)
    const [airports, setAirports] = useState([]);
    // Stare pentru zborul aflat în editare
    const [editFlight, setEditFlight] = useState(null);
    // Stare pentru zborurile favorite
    const [favoriteFlights, setFavoriteFlights] = useState([]);
    // Removed unused variable email
    // const email = localStorage.getItem("userEmail");

    // State-uri pentru datele noului zbor din formularul de adăugare
    const [newFlightData, setNewFlightData] = useState({
        airline: '',
        departureAirport: '',
        arrivalAirport: '',
        flightDuration: '',
        departureTime: '',
        arrivalTime: '',
        totalSeats: '',
        pricePerSeat: '',
    });

    // State-uri pentru erorile formularului de adăugare
    const [newFlightErrors, setNewFlightErrors] = useState({});

    // State-uri pentru datele zborului aflat în editare
    const [editFlightData, setEditFlightData] = useState(null);

    // State-uri pentru erorile formularului de editare
    const [editFlightErrors, setEditFlightErrors] = useState({});


    // Obține aeroporturile la montarea componentei
    useEffect(() => {
        fetch('http://localhost:8080/airport')
            .then(response => response.json())
            .then(data => {
                console.log("Aeroporturi primite:", data);
                setAirports(data);
            })
            .catch(error => {
                console.error("Eroare la fetch aeroporturi:", error);
            });
    }, []);

    // Obține zborurile la montarea componentei
    useEffect(() => {
        fetch('http://localhost:8080/flight')
            .then(response => response.json())
            .then(data => {
                console.log("Date primite:", data);
                setFlights(data);
            })
            .catch(error => {
                console.error("Eroare la fetch zboruri:", error);
            });
    }, []);

    // Obține zborurile favorite la montarea componentei
    useEffect(() => {
        if (currentUser?.id) {
            fetch(`http://localhost:8080/user/${currentUser.id}/favorites`)
                .then(response => response.json())
                .then(data => {
                    setFavoriteFlights(data);
                })
                .catch(error => {
                    console.error("Eroare la fetch favorite:", error);
                });
        }
    }, [currentUser]);

    // Inițializează datele formularului de editare când se selectează un zbor pentru editare
    useEffect(() => {
        if (editFlight) {
            setEditFlightData({
                id: editFlight.id,
                airline: editFlight.airline || '',
                departureAirport: editFlight.departureAirport?.id || '',
                arrivalAirport: editFlight.arrivalAirport?.id || '',
                flightDuration: editFlight.flightDuration || '',
                departureTime: editFlight.departureTime || '',
                arrivalTime: editFlight.arrivalTime || '',
                totalSeats: editFlight.totalSeats || '',
                pricePerSeat: editFlight.pricePerSeat || '',
            });
            setEditFlightErrors({}); // Resetează erorile la deschiderea formularului de editare
        }
    }, [editFlight]);


    // Verifică dacă un zbor este favorit
    const isFlightFavorite = (flightId) => {
        return favoriteFlights.some(flight => flight.id === flightId);
    };

    // Adaugă sau șterge un zbor din favorite
    const toggleFavorite = async (flightId) => {
        if (!currentUser?.id) {
            alert("Trebuie să fii autentificat pentru a adăuga favorite!");
            return;
        }

        const isFavorite = isFlightFavorite(flightId);
        const method = isFavorite ? 'DELETE' : 'POST';

        try {
            const response = await fetch(`http://localhost:8080/user/${currentUser.id}/favorites/${flightId}`, {
                method: method
            });

            if (response.ok) {
                if (isFavorite) {
                    setFavoriteFlights(prev => prev.filter(f => f.id !== flightId));
                } else {
                    const flight = flights.find(f => f.id === flightId);
                    setFavoriteFlights(prev => [...prev, flight]);
                }
            }
        } catch (error) {
            console.error("Eroare la actualizarea favoritelor:", error);
        }
    };

    // Handler pentru schimbarea valorilor din formularul de adăugare
    const handleNewFlightChange = (e) => {
        const { name, value } = e.target;
        setNewFlightData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    // Handler pentru schimbarea valorilor din formularul de editare
    const handleEditFlightChange = (e) => {
        const { name, value } = e.target;
        setEditFlightData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };


    // Adaugă un nou zbor
    const addFlight = async (newFlight) => {
        try {
            const response = await fetch('http://localhost:8080/flight', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newFlight),
            });

            if (!response.ok) {
                // Read error message from backend if response is not OK
                const errorData = await response.json(); // Assuming backend sends JSON errors
                // Handle backend validation errors and display them
                if (response.status === 400 && errorData) {
                    setNewFlightErrors(errorData); // Assuming errorData is a map of field -> error
                    alert("Backend validation failed. Check fields for errors.");
                } else {
                    alert("Failed to add flight: " + (errorData.message || response.statusText));
                }
                console.error("Eroare la adăugare zbor:", errorData);
                return;
            }

            const data = await response.json();
            setFlights(prevFlights => [...prevFlights, data]); // Adaugă noul zbor în listă
            setNewFlightData({ // Resetează formularul
                airline: '',
                departureAirport: '',
                arrivalAirport: '',
                flightDuration: '',
                departureTime: '',
                arrivalTime: '',
                totalSeats: '',
                pricePerSeat: '',
            });
            setNewFlightErrors({}); // Resetează erorile după succes
            alert("Zbor adăugat cu succes!");

        } catch (error) {
            console.error("Eroare la adăugare zbor:", error);
            alert("A apărut o eroare la adăugarea zborului.");
        }
    };

    // Gestionează trimiterea formularului pentru adăugare
    const handleAddSubmit = async (e) => {
        e.preventDefault();

        // Reset errors
        setNewFlightErrors({});

        try {
            const departureAirport = airports.find(a => a.id === Number(newFlightData.departureAirport));
            const arrivalAirport = airports.find(a => a.id === Number(newFlightData.arrivalAirport));

            const flightToAdd = {
                airline: newFlightData.airline,
                departureAirport: { id: departureAirport.id },
                arrivalAirport: { id: arrivalAirport.id },
                flightDuration: Number(newFlightData.flightDuration),
                departureTime: newFlightData.departureTime + ":00",
                arrivalTime: newFlightData.arrivalTime + ":00",
                totalSeats: Number(newFlightData.totalSeats),
                pricePerSeat: Number(newFlightData.pricePerSeat)
            };

            const response = await fetch('http://localhost:8080/flight', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(flightToAdd),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to add flight');
            }

            const data = await response.json();
            setFlights(prev => [...prev, data]);

            // Reset form
            setNewFlightData({
                airline: '',
                departureAirport: '',
                arrivalAirport: '',
                flightDuration: '',
                departureTime: '',
                arrivalTime: '',
                totalSeats: '',
                pricePerSeat: '',
            });

            alert('Flight added successfully!');
        } catch (error) {
            console.error('Error adding flight:', error);
            alert(error.message || 'Error adding flight');
        }
    };
    // Șterge un zbor după id
    const deleteFlight = (id) => {
        fetch(`http://localhost:8080/flight/${id}`, {
            method: 'DELETE',
        })
            .then(() => {
                setFlights(prevFlights => prevFlights.filter(flight => flight.id !== id)); // Elimină zborul șters din listă
            })
            .catch(error => console.error("Eroare la ștergerea zborului:", error));
    };

    // Trimite la backend modificările pentru un zbor
    const updateFlight = async (updatedFlight) => {
        try {
            console.log("Sending update request:", updatedFlight);
            const response = await fetch(`http://localhost:8080/flight/${updatedFlight.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(updatedFlight),
            });

            if (!response.ok) {
                const errorData = await response.json();
                console.error("Update error response:", errorData);
                throw new Error(errorData.message || 'Failed to update flight');
            }

            const data = await response.json();
            console.log("Update successful:", data);
            return data;
        } catch (error) {
            console.error("Error updating flight:", error);
            throw error;
        }
    };

    // Validare frontend comună pentru formularele de zbor
    const validateFlightForm = (flightData) => {
        const errors = {};
        let formIsValid = true;

        if (!flightData.airline) {
            formIsValid = false;
            errors.airline = 'Airline is required.';
        }

        if (!flightData.departureAirport) {
            formIsValid = false;
            errors.departureAirport = 'Departure airport is required.';
        }

        if (!flightData.arrivalAirport) {
            formIsValid = false;
            errors.arrivalAirport = 'Arrival airport is required.';
        }

        if (flightData.departureAirport && flightData.arrivalAirport && flightData.departureAirport === flightData.arrivalAirport) {
            formIsValid = false;
            errors.arrivalAirport = 'Departure and arrival airports cannot be the same.';
        }

        const flightDuration = Number(flightData.flightDuration);
        if (isNaN(flightDuration) || flightDuration < 0) {
            formIsValid = false;
            errors.flightDuration = 'Duration must be a non-negative number.';
        }

        const departureTime = new Date(flightData.departureTime);
        if (!flightData.departureTime || isNaN(departureTime.getTime())) {
            formIsValid = false;
            errors.departureTime = 'Departure time is required and must be valid.';
        } else if (departureTime < new Date()) {
            formIsValid = false;
            errors.departureTime = 'Departure time cannot be in the past.';
        }

        const arrivalTime = new Date(flightData.arrivalTime);
        if (!flightData.arrivalTime || isNaN(arrivalTime.getTime())) {
            formIsValid = false;
            errors.arrivalTime = 'Arrival time is required and must be valid.';
        } else if (flightData.departureTime && arrivalTime <= departureTime) {
            formIsValid = false;
            errors.arrivalTime = 'Arrival time must be after departure time.';
        }

        const totalSeats = Number(flightData.totalSeats);
        if (isNaN(totalSeats) || totalSeats < 1) {
            formIsValid = false;
            errors.totalSeats = 'Total seats must be at least 1.';
        }

        const pricePerSeat = Number(flightData.pricePerSeat);
        if (isNaN(pricePerSeat) || pricePerSeat < 0) {
            formIsValid = false;
            errors.pricePerSeat = 'Price per seat must be a non-negative number.';
        }


        return { errors, formIsValid };
    };


    return (
        <div style={{ padding: '20px' }}>
            <h2 style={{ textAlign: 'center', color: '#2c3e50' }}>Lista Zborurilor</h2>

            {/* Formular pentru adăugare zbor */}
            <div>
                <h3>Adaugă un zbor nou</h3>
                <form onSubmit={handleAddSubmit}>
                    <div>
                        <input type="text" name="airline" placeholder="Companie aeriană" value={newFlightData.airline} onChange={handleNewFlightChange} required />
                        {newFlightErrors.airline && <div style={{ color: 'red' }}>{newFlightErrors.airline}</div>}
                    </div>
                    <div>
                        <input type="number" name="flightDuration" placeholder="Durata zborului (minute)" value={newFlightData.flightDuration} onChange={handleNewFlightChange} required />
                        {newFlightErrors.flightDuration && <div style={{ color: 'red' }}>{newFlightErrors.flightDuration}</div>}
                    </div>
                    <div>
                        <input type="datetime-local" name="departureTime" value={newFlightData.departureTime} onChange={handleNewFlightChange} required />
                        {newFlightErrors.departureTime && <div style={{ color: 'red' }}>{newFlightErrors.departureTime}</div>}
                    </div>
                    <div>
                        <input type="datetime-local" name="arrivalTime" value={newFlightData.arrivalTime} onChange={handleNewFlightChange} required />
                        {newFlightErrors.arrivalTime && <div style={{ color: 'red' }}>{newFlightErrors.arrivalTime}</div>}
                    </div>
                    <div>
                        <input type="number" name="totalSeats" placeholder="Număr locuri" value={newFlightData.totalSeats} onChange={handleNewFlightChange} required />
                        {newFlightErrors.totalSeats && <div style={{ color: 'red' }}>{newFlightErrors.totalSeats}</div>}
                    </div>
                    <div>
                        <input type="number" name="pricePerSeat" placeholder="Preț per loc" value={newFlightData.pricePerSeat} onChange={handleNewFlightChange} required />
                        {newFlightErrors.pricePerSeat && <div style={{ color: 'red' }}>{newFlightErrors.pricePerSeat}</div>}
                    </div>

                    {/* Dropdown pentru aeroportul de plecare */}
                    <div>
                        <select name="departureAirport" value={newFlightData.departureAirport} onChange={handleNewFlightChange} required>
                            <option value="">Alege aeroport de plecare</option>
                            {airports.map((airport) => (
                                <option key={airport.id} value={airport.id}>
                                    {airport.name} - {airport.city}, {airport.country}
                                </option>
                            ))}
                        </select>
                        {newFlightErrors.departureAirport && <div style={{ color: 'red' }}>{newFlightErrors.departureAirport}</div>}
                    </div>

                    {/* Dropdown pentru aeroportul de sosire */}
                    <div>
                        <select name="arrivalAirport" value={newFlightData.arrivalAirport} onChange={handleNewFlightChange} required>
                            <option value="">Alege aeroport de destinație</option>
                            {airports.map((airport) => (
                                <option key={airport.id} value={airport.id}>
                                    {airport.name} - {airport.city}, {airport.country}
                                </option>
                            ))}
                        </select>
                        {newFlightErrors.arrivalAirport && <div style={{ color: 'red' }}>{newFlightErrors.arrivalAirport}</div>}
                    </div>

                    <button type="submit">Adaugă Zbor</button>
                </form>
            </div>

            {/* Tabelul cu zborurile existente */}
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                <tr>
                    <th>Favorite</th>
                    <th>Companie Aeriană</th>
                    <th>Aeroport Plecare</th>
                    <th>Aeroport Destinație</th>
                    <th>Durată (minute)</th>
                    <th>Data Plecare</th>
                    <th>Data Sosire</th>
                    <th>Locuri disponibile</th>
                    <th>Preț per loc</th>
                    <th>Acțiuni</th>
                </tr>
                </thead>
                <tbody>
                {flights.map((flight) => (
                    <tr key={flight.id}>
                        <td style={{ textAlign: 'center' }}>
                            <button
                                onClick={() => toggleFavorite(flight.id)}
                                style={heartButtonStyle}
                                title={isFlightFavorite(flight.id) ? "Șterge de la favorite" : "Adaugă la favorite"}
                            >
                                {isFlightFavorite(flight.id) ? '❤️' : '🤍'}
                            </button>
                        </td>
                        <td>{flight.airline}</td>
                        <td>{flight.departureAirport?.name || "N/A"}</td>
                        <td>{flight.arrivalAirport?.name || "N/A"}</td>
                        <td>{flight.flightDuration}</td>
                        <td>{flight.departureTime}</td>
                        <td>{flight.arrivalTime}</td>
                        <td>{flight.totalSeats}</td>
                        <td>{flight.pricePerSeat}</td>
                        <td>
                            <button onClick={() => deleteFlight(flight.id)}>Șterge</button>
                            <button onClick={() => setEditFlight(flight)}>Editează</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* Formular pentru editare zbor */}
            {editFlightData && (
                <div>
                    <h3>Editează zborul</h3>
                    <form onSubmit={(e) => {
                        e.preventDefault();

                        // Frontend Validation Logic for Edit Form
                        const { errors, formIsValid } = validateFlightForm(editFlightData);

                        setEditFlightErrors(errors);

                        if (formIsValid) {
                            // Găsim aeroporturile complete din lista de aeroporturi
                            const departureAirport = airports.find(a => a.id === Number(editFlightData.departureAirport));
                            const arrivalAirport = airports.find(a => a.id === Number(editFlightData.arrivalAirport));

                            const updatedFlightToSend = {
                                id: editFlightData.id,
                                airline: editFlightData.airline,
                                departureAirport: {
                                    id: departureAirport.id,
                                    name: departureAirport.name,
                                    city: departureAirport.city,
                                    country: departureAirport.country
                                },
                                arrivalAirport: {
                                    id: arrivalAirport.id,
                                    name: arrivalAirport.name,
                                    city: arrivalAirport.city,
                                    country: arrivalAirport.country
                                },
                                flightDuration: Number(editFlightData.flightDuration),
                                departureTime: editFlightData.departureTime,
                                arrivalTime: editFlightData.arrivalTime,
                                totalSeats: Number(editFlightData.totalSeats),
                                pricePerSeat: Number(editFlightData.pricePerSeat)
                            };
                            updateFlight(updatedFlightToSend); // Trimite zborul modificat la backend
                        }

                    }}>
                        <div>
                            <label>Companie aeriană:</label>
                            <input type="text" name="airline" placeholder="Companie aeriană" value={editFlightData.airline} onChange={handleEditFlightChange} required />
                            {editFlightErrors.airline && <div style={{ color: 'red' }}>{editFlightErrors.airline}</div>}
                        </div>
                        <div>
                            <label>Durată (minute):</label>
                            <input type="number" name="flightDuration" placeholder="Durata zborului (minute)" value={editFlightData.flightDuration} onChange={handleEditFlightChange} required />
                            {editFlightErrors.flightDuration && <div style={{ color: 'red' }}>{editFlightErrors.flightDuration}</div>}
                        </div>
                        <div>
                            <label>Data Plecare:</label>
                            <input type="datetime-local" name="departureTime" value={editFlightData.departureTime} onChange={handleEditFlightChange} required />
                            {editFlightErrors.departureTime && <div style={{ color: 'red' }}>{editFlightErrors.departureTime}</div>}
                        </div>
                        <div>
                            <label>Data Sosire:</label>
                            <input type="datetime-local" name="arrivalTime" value={editFlightData.arrivalTime} onChange={handleEditFlightChange} required />
                            {editFlightErrors.arrivalTime && <div style={{ color: 'red' }}>{editFlightErrors.arrivalTime}</div>}
                        </div>
                        <div>
                            <label>Număr locuri:</label>
                            <input type="number" name="totalSeats" placeholder="Număr locuri" value={editFlightData.totalSeats} onChange={handleEditFlightChange} required />
                            {editFlightErrors.totalSeats && <div style={{ color: 'red' }}>{editFlightErrors.totalSeats}</div>}
                        </div>
                        <div>
                            <label>Preț per loc:</label>
                            <input type="number" name="pricePerSeat" placeholder="Preț per loc" value={editFlightData.pricePerSeat} onChange={handleEditFlightChange} required />
                            {editFlightErrors.pricePerSeat && <div style={{ color: 'red' }}>{editFlightErrors.pricePerSeat}</div>}
                        </div>

                        {/* Dropdown pentru aeroportul de plecare */}
                        <div>
                            <label>Aeroport Plecare:</label>
                            <select name="departureAirport" value={editFlightData.departureAirport} onChange={handleEditFlightChange} required>
                                <option value="">Alege aeroport de plecare</option>
                                {airports.map((airport) => (
                                    <option key={airport.id} value={airport.id}>
                                        {airport.name} - {airport.city}, {airport.country}
                                    </option>
                                ))}
                            </select>
                            {editFlightErrors.departureAirport && <div style={{ color: 'red' }}>{editFlightErrors.departureAirport}</div>}
                        </div>

                        {/* Dropdown pentru aeroportul de sosire */}
                        <div>
                            <label>Aeroport Destinație:</label>
                            <select name="arrivalAirport" value={editFlightData.arrivalAirport} onChange={handleEditFlightChange} required>
                                <option value="">Alege aeroport de destinație</option>
                                {airports.map((airport) => (
                                    <option key={airport.id} value={airport.id}>
                                        {airport.name} - {airport.city}, {airport.city}
                                    </option>
                                ))}
                            </select>
                            {editFlightErrors.arrivalAirport && <div style={{ color: 'red' }}>{editFlightErrors.arrivalAirport}</div>}
                        </div>

                        <button type="submit">Salvează Modificările</button>
                        <button onClick={() => setEditFlight(null)}>Anulează</button>
                    </form>
                </div>
            )}

            {/* Butonul de delogare */}
            <button onClick={onLogout} style={logoutButtonStyle}>LogOut</button>
        </div>
    );
}

// Stil pentru butonul de logout
const logoutButtonStyle = {
    marginTop: '20px',
    padding: '10px 20px',
    backgroundColor: '#ff4d4d',
    color: 'white',
    border: 'none',
    cursor: 'pointer',
};

export default FlightList;