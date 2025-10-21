import React, { useEffect, useState } from 'react';

function Bookings() {
    const [bookings, setBookings] = useState([]);

    useEffect(() => {
        const fetchBookings = async () => {
            try {
                const email = localStorage.getItem("email");

                if (!email) {
                    alert("No user logged in");
                    return;
                }

                const response = await fetch(`http://localhost:8080/booking/user/bookings?email=${email}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                });

                if (response.ok) {
                    const data = await response.json();
                    setBookings(data);
                } else {
                    alert("Failed to load bookings");
                }
            } catch (error) {
                console.error("Error fetching bookings:", error);
            }
        };

        fetchBookings();
    }, []);

    return (
        <div>
            <h1>Rezervările tale</h1>

            {bookings.length > 0 ? (
                <ul>
                    {bookings.map((booking, index) => (
                        <li key={index}>
                            Zbor:{" "}
                            {booking.departureAirport?.name && booking.arrivalAirport?.name ? (
                                `${booking.departureAirport.name} - ${booking.arrivalAirport.name}`
                            ) : (
                                "Informații zbor indisponibile"
                            )}
                            , Locuri: {booking.numberOfSeats}, Preț: {booking.totalPrice} RON
                        </li>
                    ))}
                </ul>
            ) : (
                <p>Nu aveți nicio rezervare.</p>
            )}
        </div>
    );
}

export default Bookings;
