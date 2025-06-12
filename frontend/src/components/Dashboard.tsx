import {useNavigate} from "react-router-dom";

const Dashboard = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/");
    };

    const navigateToRegisterUser = () => {
        navigate("/registeruser")
    }

    return (
        <>
            <button onClick={navigateToRegisterUser}>Register user</button>
            <button onClick={handleLogout}>Log out</button>
        </>
    )
};

export default Dashboard;