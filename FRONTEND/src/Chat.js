import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

function Chat() {
    const [messages, setMessages] = useState([]);
    const [messageInput, setMessageInput] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [isConnected, setIsConnected] = useState(false);
    const messagesEndRef = useRef(null);

    // Obține email-ul utilizatorului din localStorage
    const userEmail = sessionStorage.getItem("userEmail") || 'Anonymous';


    useEffect(() => {
        // Ia istoricul la încărcare
        fetch("http://localhost:8080/chat/history")
            .then(res => res.json())
            .then(data => setMessages(data))
            .catch(err => console.error("Eroare la preluare istoric:", err));
        const socket = new SockJS('http://localhost:8080/socket');
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            debug: (str) => console.log(str),
        });

        client.onConnect = () => {
            console.log('Connected to chat!');
            setIsConnected(true);

            client.subscribe('/topic/messages', (message) => {
                const receivedMessage = JSON.parse(message.body);
                console.log('Received message:', receivedMessage);
                setMessages(prev => [...prev, receivedMessage]);
            });
        };

        client.onStompError = (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        };

        client.activate();
        setStompClient(client);

        return () => {
            if (client) {
                client.deactivate();
                setIsConnected(false);
            }
        };
    }, []);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useEffect(scrollToBottom, [messages]);

    const sendMessage = (e) => {
        e.preventDefault();
        if (!messageInput.trim() || !stompClient || !isConnected) {
            console.log('Cannot send message - not ready');
            return;
        }

        const chatMessage = {
            sender: userEmail, // Folosește email-ul din localStorage
            content: messageInput,
            timestamp: new Date().toLocaleTimeString()
        };

        console.log('Sending message:', chatMessage);

        stompClient.publish({
            destination: '/app/chat',
            body: JSON.stringify(chatMessage),
            headers: { 'content-type': 'application/json' }
        });

        setMessageInput('');
    };

    return (
        <div className="chat-container">
            <div className="messages-container">
                {messages.map((msg, index) => (
                    <div key={index} className="message">
                        <strong>{msg.sender}</strong>: {msg.content}
                        <small>{msg.timestamp}</small>
                    </div>
                ))}
                <div ref={messagesEndRef} />
            </div>
            <form onSubmit={sendMessage} className="message-form">
                <input
                    type="text"
                    value={messageInput}
                    onChange={(e) => setMessageInput(e.target.value)}
                    placeholder="Scrie un mesaj..."
                />
                <button type="submit" disabled={!isConnected}>
                    {isConnected ? 'Trimite' : 'Conectând...'}
                </button>
            </form>
            <div className="connection-status">
                Status: {isConnected ? 'Conectat' : 'Deconectat'}
            </div>
        </div>
    );
}

export default Chat;