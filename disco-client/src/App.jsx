import './App.css'
import {useState} from "react";
import ReactPlayer from "react-player";
import useWebSocket from "react-use-websocket";

const socketUrl = "ws://localhost:8080/ws"

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

    const [videoUrl, setVideoUrl] = useState("")
    const [audioPath, setAudioPath] = useState("")
    const [playing, setPlaying] = useState(false)

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
            onClose: () => {
                setConnected(false)
                setVideoUrl("")
            },
            onMessage: (message) => handleMessage(message)
        },
        connectionStatus
    )

    function handleMessage(message) {
        const messageData = JSON.parse(message.data)

        switch (messageData.type) {
            case "PLAYER_VALIDATION":
                handlePlayerValidation(messageData.payload)
                break
            case "COMMAND":
                handleVideoCommand(messageData.payload)
                break
            case "LINK":
                handleVideoLink(messageData.payload)
                break
        }
    }

    function handleVideoLink(payload) {
        setVideoUrl(payload.url)
    }

    function handleVideoCommand(payload) {
        switch (payload.command) {
            case "play":
                setPlaying(true)
                break
            case "pause":
                setPlaying(false)
                setAudioPath("/backspin.mp3")
                break
            case "loose":
                setAudioPath("/aww.mp3")
                break
            case "winner":
                setAudioPath("/yippee.mp3")
                break
            case "alive":
                setAudioPath("/yaay.mp3")
                break
        }
    }

    function handleConnection() {
        setConnectionStatus(!connectionStatus)
    }

    function validatePlayer(playerName) {
        sendMessage(mountMessage("PLAYER_VALIDATION", { playerName }))
    }

    function handlePlayerValidation(payload) {
        if (payload.reply === false) {
            setInvalidPlayer(true)
            handleConnection()
            return
        }

        setConnected(true)
        setInvalidPlayer(false)
    }

    return (
        <div>
            <h1>DOKUSHAISHANG PROJECT</h1>

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

            {connected && <img src={"https://mc-heads.net/avatar/" + playerName + ".png"} alt=""/> }

            {invalidPlayer && <p style={{ color: 'red' }}>PLAYER NOT FOUND</p> }

            <ReactPlayer
                playing={playing}
                controls={false}
                url={videoUrl}
                loop={true}
            />

            <ReactPlayer
                playing={true}
                url={audioPath}
            />
        </div>
    )
}

export default App
