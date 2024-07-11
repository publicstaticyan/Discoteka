import './App.css'
import {useRef, useState} from "react";

function App() {

    const [message, setMessage] = useState("")
    const [connected, setConnected] = useState(false)
    const [receivedMessages, setReceivedMessages] = useState([])
    const pingInterval = useRef(null)
    const socket = useRef(null)

    function createConnection() {
        if (socket.current === undefined || socket.current === null) {
            socket.current = new WebSocket("ws://localhost:8080/ws")

            socket.current.onopen = () => {
                setConnected(true)
                startPinging()
            }

            socket.current.onclose = () => {
                setConnected(false)
                clearInterval(pingInterval.current)
            }

            socket.current.onmessage = (message) => {
                setReceivedMessages((prevMessages) => [...prevMessages, message.data])
            }
        }
    }

    function startPinging() {
        pingInterval.current = setInterval(() => {
            if (socket.current.readyState === WebSocket.OPEN) {
                socket.current.send("ping")
            }
        }, 5000)
    }

    function sendMessage() {
        socket.current.send(message)
    }

    return (
        <div>
            <h1>Discoteka</h1>

            <input
                disabled={!connected}
                onChange={(event) => setMessage(event.target.value)}
                value={message}/>

            <button
                disabled={!connected}
                onClick={sendMessage}
                type="submit"
            >
                Send Message
            </button>

            <br/>
            <br/>

            <button
                disabled={connected}
                onClick={createConnection}
                type="submit"
            >
                Connect Socket
            </button>

            <br/>

            <p>Connection status:
                <span style={{ color: connected ? 'green' : 'red'}}> {connected ? "CONNECTED" : "DISCONNECTED"}</span>
            </p>

            <br/>
            <br/>

            <p>Messages:</p>
            <ul>
                {receivedMessages.map((message, index) => (
                    <li key={index}>{message}</li>
                ))}
            </ul>
        </div>
    )
}

export default App
