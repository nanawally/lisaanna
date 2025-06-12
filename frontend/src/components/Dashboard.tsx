import {useNavigate} from "react-router-dom";

const Dashboard = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/");
    };

    return (
        <>
            <a href="/registeruser">Register user</a>
            <button onClick={handleLogout}>Log out</button>
        </>

    )
};

export default Dashboard;