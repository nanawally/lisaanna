import {useNavigate} from "react-router-dom";
import {useState} from "react";

const LoginPage = () => {
    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async () => {
        try {
            const response = await fetch(
                "http://localhost:8080/request-token",
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ username, password }),
                }
            );
            //vid felaktig inloggnign
            if (!response.ok) {
                throw new Error("Felaktiga inloggningsuppgifter!");
            }
            //hämta token från responsen
            const token = await response.text();
            //spara jwt token
            localStorage.setItem("token", token);
            console.log(token);
            //yeeta amvändaren till dashboard
            navigate("/dashboard");
        } catch (error) {
            if (error instanceof Error) {
                alert(error.message);
            }
        }
    };

    return (
        <>
            <input type="text" placeholder="Användarnamn" onChange={(e) => setUsername(e.target.value)} />
            <input type="password" placeholder="Lösenord" onChange={(e) => setPassword(e.target.value)} />
            <button onClick={handleLogin}>Logga in</button>
        </>
    );
};

export default LoginPage;