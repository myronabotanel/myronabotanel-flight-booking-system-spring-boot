// Importă React și hook-urile necesare din bibliotecă
import React, { useState, useEffect } from 'react';
// Importă hook-ul pentru accesarea locației URL-ului (query parameters)
import { useLocation } from 'react-router-dom';

function BuyTicket() {// Obține partea de query string din URL (ex: ?flightId=123)
    const { search } = useLocation();// Creează un obiect pentru a prelua parametrii din query string
    const params = new URLSearchParams(search);// Extrage valoarea parametrului 'flightId' din query string
    const flightId = params.get('flightId');

    // Starea pentru detaliile zborului
    const [flight, setFlight] = useState(null);
    const [numTickets, setNumTickets] = useState(1);// Starea pentru numărul de bilete selectate
    const [totalPrice, setTotalPrice] = useState(0);  // Starea pentru prețul total calculat
    const [error, setError] = useState(null);// Starea pentru afișarea unei erori
    const [message, setMessage] = useState(null);// Starea pentru afișarea unui mesaj de succes sau eșec
    const [username, setUsername] = useState('');// Starea pentru numele utilizatorului (email)

    // Hook pentru incarcarea emailului salvat din localStorage la montarea componentului
    useEffect(() => {
        const savedEmail = localStorage.getItem('email');
        if (savedEmail) {
            setUsername(savedEmail);
        }
    }, []);

    // Hook pentru a obține detalii despre zbor de la backend
    useEffect(() => {
        const fetchFlightDetails = async () => {
            try {
                // Trimite request GET către backend pentru detalii zbor
                const response = await fetch(`http://localhost:8080/flight/${flightId}`);
                if (response.ok) {
                    const data = await response.json(); // Parsează răspunsul JSON
                    setFlight(data); // Salvează detaliile zborului în stare
                    // Calculează prețul total în funcție de numărul de bilete
                    setTotalPrice(data.pricePerSeat * numTickets);
                } else {
                    // Dacă zborul nu a fost găsit
                    setError('Zborul nu a fost găsit.');
                }
            } catch (err) {
                // În caz de eroare la fetch
                setError('A apărut o eroare la încărcarea detaliilor zborului.');
            }
        };

        fetchFlightDetails(); // Apelează funcția definită
    }, [flightId, numTickets]); // Se reexecută dacă se schimbă flightId sau numTickets

    // Funcția care trimite cererea de cumpărare a biletului
    const handleBuyTicket = async () => {
        try {
            // Trimite request POST către backend pentru a cumpăra biletul
            const response = await fetch(
                `http://localhost:8080/booking/purchase?flightId=${flightId}&username=${username}&numberOfSeats=${numTickets}`,
                { method: 'POST' }
            );

            const data = await response.text(); // Primește mesajul text (nu JSON)

            if (response.ok) {
                setMessage(data); // Afișează mesajul de succes
            } else {
                setMessage(data); // Afișează mesajul de eroare
            }
        } catch (err) {
            setError('A apărut o eroare la procesarea achiziției.');
        }
    };

    // Dacă există o eroare, afișeaz-o
    if (error) {
        return <p style={{ color: 'red' }}>{error}</p>;
    }

    // Dacă datele zborului nu au fost încă încărcate, afișează mesaj de așteptare
    if (!flight) {
        return <p>Se încarcă...</p>;
    }

    // Returnează interfața grafică a componentei
    return (
        <div style={{ padding: '20px' }}>
            {/* Afișează un mesaj de bun venit utilizatorului */}
            <div style={{ marginBottom: '20px' }}>
                <h3>Bun venit, {username}!</h3>
            </div>

            <h2>Detalii Zbor</h2>
            {/* Afișează detaliile zborului */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                <div><strong>Companie:</strong> {flight.airline}</div>
                <div><strong>Ruta:</strong> {flight.departureAirport.city} → {flight.arrivalAirport.city}</div>
                <div><strong>Plecare:</strong> {new Date(flight.departureTime).toLocaleString()}</div>
                <div><strong>Sosire:</strong> {new Date(flight.arrivalTime).toLocaleString()}</div>
                <div><strong>Durata:</strong> {flight.flightDuration} minute</div>
                {/* Calculează și afișează locurile rămase */}
                <div><strong>Locuri disponibile:</strong> {flight.totalSeats - (flight.bookings?.length || 0)} / {flight.totalSeats}</div>
                <div><strong>Preț pe bilet:</strong> {flight.pricePerSeat} RON</div>

                {/* Selector pentru numărul de bilete */}
                <div>
                    <strong>Număr de bilete:</strong>{' '}
                    <input
                        type="number"
                        value={numTickets}
                        onChange={(e) => setNumTickets(Number(e.target.value))} // Actualizează numărul de bilete
                        min="1"
                        max={flight.totalSeats - (flight.bookings?.length || 0)} // Maxim locuri disponibile
                        required
                    />
                </div>

                {/* Afișează prețul total */}
                <div><strong>Preț total:</strong> {totalPrice} RON</div>
            </div>

            {/* Afișează mesajul rezultat după cumpărare */}
            {message && (
                <p style={{ color: message.includes('cu succes') ? 'green' : 'red' }}>
                    {message}
                </p>
            )}

            {/* Butonul de cumpărare */}
            <div style={{ marginTop: '20px' }}>
                <button
                    style={{
                        padding: '10px 20px',
                        backgroundColor: '#4CAF50',
                        color: 'white',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                    }}
                    onClick={handleBuyTicket} // Când se apasă, cumpără biletul
                >
                    Cumpără Bilet
                </button>
            </div>
        </div>
    );
}

// Exportă componenta pentru a fi utilizată în alte fișiere
export default BuyTicket;
