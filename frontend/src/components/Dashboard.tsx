import {useNavigate} from "react-router-dom";

const Dashboard = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/");
    };

    return <button onClick={handleLogout}>Logga ut</button>
};

export default Dashboard;