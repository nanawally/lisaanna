import {useNavigate} from "react-router-dom";

const Dashboard = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/");
    };

    return (
        <>
            <a href="/registeruser">Registrera användare</a>
            <button onClick={handleLogout}>Logga ut</button>
        </>

    )
};

export default Dashboard;