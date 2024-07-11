import './App.css'
import {useState} from "react";
import ReactPlayer from "react-player";
import useWebSocket from "react-use-websocket";

const socketUrl = "ws://localhost:8082/ws"

function mountMessage(type, payload) {
    return JSON.stringify({
        type,
        payload
    })
}

function App() {

    const [connectionStatus, setConnectionStatus] = useState(false)

    const [playerName, setPlayerName] = useState("")
    const [invalidPlayer, setInvalidPlayer] = useState(false)
    const [connected, setConnected] = useState(false)

    const { sendMessage } = useWebSocket(
        socketUrl,
        {
            heartbeat: {
                message: mountMessage("PING", {}),
                returnMessage: mountMessage("PING", {reply: "pong"}),
                timeout: '60000',
                interval: '10000'
            },
            shouldReconnect: () => false,
            onOpen: () => validatePlayer(playerName),
            onClose: () => setConnected(false),
            onMessage: (message) => handleMessage(message)
        },
        connectionStatus
    )

    function handleMessage(message) {
        const messageData = JSON.parse(message.data)

        switch (messageData.type) {
            case "PLAYER_VALIDATION":
                handlePlayerValidation(messageData.payload.reply)
                break
            case "COMMAND":
                break
        }
    }

    function handleConnection() {
        setConnectionStatus(!connectionStatus)
    }

    function validatePlayer(playerName) {
        sendMessage(mountMessage("PLAYER_VALIDATION", { playerName }))
    }

    function handlePlayerValidation(reply) {
        if (reply === false) {
            setInvalidPlayer(true)
            handleConnection()
            return
        }

        setConnected(true)
        setInvalidPlayer(false)
    }

    return (
        <div>
            <h1>Discoteka</h1>

            <input
                disabled={connected}
                onChange={(event) => setPlayerName(event.target.value)}
                placeholder="Player name"
                value={playerName}/>

            <button
                onClick={handleConnection}
                type="submit"
            >
                {connected ? "Disconnect" : "Connect"}
            </button>

            <br/>
            <br/>

            <p>Connection status:
                <span style={{ color: connected ? 'green' : 'red'}}> {connected ? "CONNECTED" : "DISCONNECTED"}</span>
            </p>

            {connected ?
                <div>
                    <img src={"https://mc-heads.net/avatar/" + playerName + ".png"} alt=""/>
                </div> : <></>}

            {invalidPlayer ?
                <p style={{ color: 'red' }}>PLAYER NOT FOUND</p>
                :
                <></>
            }

            <ReactPlayer
                // playing={true}
                // controls={false}
                url={"https://www.youtube.com/watch?v=_Ca12JSMN9E"}
            />
        </div>
    )
}

export default App
