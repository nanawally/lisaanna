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
                throw new Error("Login credentials do not match");
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
            <div><input type="text" placeholder="Username" onChange={(e) => setUsername(e.target.value)} /></div>
            <div><input type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} /></div>
            <div><button onClick={handleLogin}>Log in</button></div>
        </>
    );
};

export default LoginPage;