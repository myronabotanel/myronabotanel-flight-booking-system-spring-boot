import React, { useEffect, useState } from 'react';

function AirportList({ onLogout }) {
    const [airports, setAirports] = useState([]);
    const [editAirport, setEditAirport] = useState(null);

    // Fetch aeroporturi din backend
    useEffect(() => {
        fetch('http://localhost:8080/airport') // apel către backend
            .then(response => response.json())
            .then(data => {
                console.log("Date primite:", data);
                setAirports(data);
            })
            .catch(error => {
                console.error("Eroare la fetch:", error);
            });
    }, []);

    // Adăugarea unui aeroport
    const addAirport = (newAirport) => {
        fetch('http://localhost:8080/airport', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newAirport),
        })
            .then(response => response.json())
            .then(data => {
                // Actualizează starea pentru a include aeroportul nou adăugat
                setAirports(prevAirports => [...prevAirports, data]);
            })
            .catch(error => console.error("Eroare la adăugare aeroport:", error));
    };


    // Ștergerea unui aeroport
    const deleteAirport = (id) => {
        fetch(`http://localhost:8080/airport/${id}`, { method: 'DELETE' })
            .then(() => {
                setAirports(airports.filter(airport => airport.id !== id));
            })
            .catch(error => console.error("Eroare la ștergere aeroport:", error));
    };

    // Actualizarea unui aeroport
    // Actualizarea unui aeroport
    const updateAirport = (updatedAirport) => {
        fetch(`http://localhost:8080/airport/${updatedAirport.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedAirport),
        })
            .then(response => response.json())
            .then(data => {
                // Actualizează aeroportul în listă
                setAirports(prevAirports =>
                    prevAirports.map(airport =>
                        airport.id === data.id ? data : airport
                    )
                );
                setEditAirport(null); // Închide formularul de editare
            })
            .catch(error => console.error("Eroare la actualizare aeroport:", error));
    };

    // Funcția pentru export XML
    const handleExportXml = async () => {
        try {
            const response = await fetch('http://localhost:8080/airport/export/xml', {
                headers: {
                    'Accept': 'application/xml'
                }
            });
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            const xmlText = await response.text();

            const blob = new Blob([xmlText], { type: 'application/xml' });
            const url = URL.createObjectURL(blob);

            const a = document.createElement('a');
            a.href = url;
            a.download = 'airports.xml';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);

        } catch (error) {
            console.error("Error exporting XML:", error);
            alert("Eroare la exportul XML: " + error.message);
        }
    };


    // Formularul pentru adăugarea unui aeroport
    const handleAddSubmit = (e) => {
        e.preventDefault();
        const newAirport = {
            name: e.target.name.value,
            city: e.target.city.value,
            country: e.target.country.value,
        };
        addAirport(newAirport);
        e.target.reset();
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2 style={{ textAlign: 'center', color: '#2c3e50' }}>Lista Aeroporturilor</h2>

            {/* Buton pentru Export XML */}
            <button onClick={handleExportXml} style={{ marginBottom: '20px' }}>Exportă Aeroporturi XML</button>

            {/* Formular pentru adăugare aeroport */}
            <div>
                <h3>Adaugă un aeroport nou</h3>
                <form onSubmit={handleAddSubmit}>
                    <input type="text" name="name" placeholder="Nume aeroport" required />
                    <input type="text" name="city" placeholder="Oraș" required />  {/* Modificat din location */}
                    <input type="text" name="country" placeholder="Țară" required />  {/* Modificat din location */}
                    <button type="submit">Adaugă Aeroport</button>
                </form>
            </div>

            {/* Tabel cu aeroporturi */}
            <table style={{width: "100%"}}>
                <thead>
                <tr>
                    <th>Nume</th>
                    <th>Oraș</th>
                    <th>Țară</th>
                </tr>
                </thead>
                <tbody>
                {airports.map(airport => (
                    <tr key={airport.id}>
                        <td>{airport.name}</td>
                        <td>{airport.city}</td>
                        <td>{airport.country}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* Butonul de LogOut */}
            <button onClick={onLogout} style={logoutButtonStyle}>LogOut</button>

            {/* Formular pentru editarea unui aeroport */}
            {editAirport && (
                <div>
                    <h3>Editează aeroportul</h3>
                    <form onSubmit={(e) => {
                        e.preventDefault();
                        const updatedAirport = {
                            id: editAirport.id,
                            name: e.target.name.value,
                            city: e.target.city.value,  // Înlocuiește location cu city
                            country: e.target.country.value,  // Înlocuiește location cu country
                        };
                        updateAirport(updatedAirport);
                    }}>
                        <input type="text" name="name" defaultValue={editAirport.name} required />
                        <input type="text" name="city" defaultValue={editAirport.city} required />  {/* Modificat */}
                        <input type="text" name="country" defaultValue={editAirport.country} required />  {/* Modificat */}
                        <button type="submit">Actualizează</button>
                    </form>
                </div>
            )}
        </div>
    );
}

const logoutButtonStyle = {
    marginTop: '20px',
    padding: '10px 20px',
    backgroundColor: '#ff4d4d',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    fontSize: '16px',
    transition: 'background-color 0.3s',
};

export default AirportList;
