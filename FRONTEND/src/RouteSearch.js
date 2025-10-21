import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function RouteSearch() {
    // Starea componentelor: aeroporturi, zboruri, aeroporturi selectate, zbor selectat și erori
    const [departureAirport, setDepartureAirport] = useState('');  // Aeroportul de plecare
    const [arrivalAirport, setArrivalAirport] = useState('');  // Aeroportul de destinație
    const [flights, setFlights] = useState([]);  // Zborurile găsite
    const [airports, setAirports] = useState([]);  // Lista de aeroporturi
    const [selectedFlight, setSelectedFlight] = useState(null);  // Zborul selectat
    const [maxPrice, setMaxPrice] = useState('');  // Prețul maxim pentru căutare
    const [searchType, setSearchType] = useState('route');  // Tipul de căutare: 'route' sau 'price'

    const [error, setError] = useState(null);  // Erorile care pot apărea (general)
    const [searchError, setSearchError] = useState(''); // Eroare specifică pentru căutare (ex: aeroporturi identice)

    const navigate = useNavigate();  // Hook pentru navigare

    // UseEffect pentru a încărca lista de aeroporturi de la server la prima renderizare a componentei
    useEffect(() => {
        const fetchAirports = async () => {
            try {
                const response = await fetch('http://localhost:8080/airport');  // Solicită aeroporturile de pe server
                if (response.ok) {
                    const data = await response.json();  // Dacă răspunsul este valid, salvează aeroporturile
                    setAirports(data);
                } else {
                    console.error('Eroare la încărcarea aeroporturilor');  // Dacă cererea nu este validă, loghează eroarea
                }
            } catch (error) {
                console.error('Eroare la conectarea cu serverul:', error);  // Loghează eroarea de conectare
            }
        };

        fetchAirports();  // Apelează funcția pentru a încărca aeroporturile
    }, []);  // UseEffect se execută o singură dată la montarea componentei

    // Funcția pentru a căuta zborurile în funcție de aeroporturile selectate
    const handleSubmit = async (e) => {
        e.preventDefault();  // Previne comportamentul implicit al formularului

        // Reset previous errors
        setSearchError('');
        setError(null);

        // Validare pentru căutarea după rută
        if (searchType === 'route') {
            if (!departureAirport || !arrivalAirport) {
                setSearchError('Te rugăm să selectezi atât aeroportul de plecare cât și cel de destinație.');
                return;
            }
            if (departureAirport === arrivalAirport) {
                setSearchError('Aeroportul de plecare nu poate fi același cu cel de destinație.');
                setFlights([]);
                return;
            }
        }

        // Validare pentru căutarea după preț
        if (searchType === 'price') {
            if (!maxPrice || maxPrice <= 0) {
                setSearchError('Te rugăm să introduci un preț maxim valid.');
                return;
            }
        }

        try {
            let url;
            if (searchType === 'route') {
                url = `http://localhost:8080/flight/search?fromCity=${encodeURIComponent(departureAirport)}&toCity=${encodeURIComponent(arrivalAirport)}`;
            } else {
                url = `http://localhost:8080/flight/search/price?maxPrice=${encodeURIComponent(maxPrice)}`;
            }

            console.log('Making request to:', url); // Debug log

            const response = await fetch(url);
            if (response.ok) {
                const data = await response.json();
                console.log('Received data:', data); // Debug log
                setFlights(data);
                setSearchError(null);
            } else {
                setFlights([]);
                const errorText = await response.text();
                setError('Nu s-au găsit zboruri pentru criteriile selectate.');
                console.error("Backend error:", response.status, errorText);
            }
        } catch (err) {
            console.error("Eroare la căutarea zborurilor:", err);
            setError('A apărut o eroare la procesarea cererii.');
            setFlights([]);
        }
    };

    // Funcția care gestionează selecția unui zbor
    const handleSelectFlight = (flight) => {
        setSelectedFlight(flight);  // Setează zborul selectat
    };

    // Funcția pentru a naviga la pagina de cumpărare bilet
    const handleBuyTicket = (flightId) => {
        const username = localStorage.getItem('username');  // Obține username-ul din localStorage
        navigate(`/booking/purchase?flightId=${flightId}&username=${username}`);  // Navighează la pagina de cumpărare cu parametrii necesari
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>Căutare Zboruri</h2>

            {/* Selector pentru tipul de căutare */}
            <div style={{ marginBottom: '20px' }}>
                <label>
                    <input
                        type="radio"
                        value="route"
                        checked={searchType === 'route'}
                        onChange={(e) => setSearchType(e.target.value)}
                    />
                    Căutare după rută
                </label>
                <label style={{ marginLeft: '20px' }}>
                    <input
                        type="radio"
                        value="price"
                        checked={searchType === 'price'}
                        onChange={(e) => setSearchType(e.target.value)}
                    />
                    Căutare după preț
                </label>
            </div>

            <form onSubmit={handleSubmit} style={{ marginBottom: '20px' }}>
                {searchType === 'route' ? (
                    <>
                        <select
                            value={departureAirport}
                            onChange={(e) => setDepartureAirport(e.target.value)}
                            required
                            style={{ marginRight: '10px', padding: '8px' }}
                        >
                            <option value="">Selectează Aeroport Plecare</option>
                            {airports.map((airport) => (
                                <option key={airport.id} value={airport.city}>
                                    {airport.city} ({airport.name})
                                </option>
                            ))}
                        </select>

                        <select
                            value={arrivalAirport}
                            onChange={(e) => setArrivalAirport(e.target.value)}
                            required
                            style={{ marginRight: '10px', padding: '8px' }}
                        >
                            <option value="">Selectează Aeroport Destinație</option>
                            {airports.map((airport) => (
                                <option key={airport.id} value={airport.city}>
                                    {airport.city} ({airport.name})
                                </option>
                            ))}
                        </select>
                    </>
                ) : (
                    <div>
                        <input
                            type="number"
                            value={maxPrice}
                            onChange={(e) => setMaxPrice(e.target.value)}
                            placeholder="Preț maxim (RON)"
                            required
                            min="0"
                            step="0.01"
                            style={{ padding: '8px', marginRight: '10px', width: '200px' }}
                        />
                    </div>
                )}

                <button 
                    type="submit"
                    style={{
                        padding: '8px 20px',
                        backgroundColor: '#4CAF50',
                        color: 'white',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                        fontSize: '16px'
                    }}
                >
                    Căutare
                </button>
            </form>

            {/* Afișează mesaj de eroare dacă există */}
            {searchError && <p style={{ color: 'red' }}>{searchError}</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            {/* Dacă sunt zboruri disponibile, afișează-le */}
            {flights.length > 0 && (
                <div>
                    <h3>Rezultate Zboruri</h3>
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '20px' }}>
                        {flights.map((flight) => {
                            const locuriDisponibile = flight.totalSeats - (flight.bookings?.length || 0);  // Calcularea locurilor disponibile
                            return (
                                <div
                                    key={flight.id}
                                    onClick={() => handleSelectFlight(flight)}  // Selectează zborul când este apăsat
                                    style={{
                                        padding: '20px',
                                        border: '1px solid #ddd',
                                        borderRadius: '8px',
                                        cursor: 'pointer',
                                        boxShadow: selectedFlight?.id === flight.id ? '0 0 10px rgba(0, 0, 0, 0.1)' : 'none',
                                    }}
                                >
                                    <h4>{flight.airline}</h4>
                                    <p>{flight.departureAirport?.city || 'N/A'} → {flight.arrivalAirport?.city || 'N/A'}</p>
                                    <p>Plecare: {new Date(flight.departureTime).toLocaleString()}</p>
                                    <p>Sosire: {new Date(flight.arrivalTime).toLocaleString()}</p>
                                    <p>Durata: {flight.flightDuration} minute</p>
                                    <p>Locuri disponibile: {locuriDisponibile} / {flight.totalSeats}</p>
                                    <p>Preț pe bilet: {flight.pricePerSeat} RON</p>
                                    {/* Butonul pentru cumpărare apare doar pentru zborul selectat */}
                                    {selectedFlight?.id === flight.id && (
                                        <button
                                            onClick={() => handleBuyTicket(flight.id)}
                                            style={{
                                                padding: '10px 20px',
                                                backgroundColor: '#4CAF50',
                                                color: 'white',
                                                border: 'none',
                                                borderRadius: '5px',
                                                cursor: 'pointer',
                                            }}
                                        >
                                            Cumpără Bilet
                                        </button>
                                    )}
                                </div>
                            );
                        })}
                    </div>
                </div>
            )}
        </div>
    );
}

export default RouteSearch;
