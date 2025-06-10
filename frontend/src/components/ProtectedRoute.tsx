import {Navigate, Outlet} from "react-router-dom";

const ProtectedRoute = () => {
    //hämta jwt token från api och sparar i local storage
    const token = localStorage.getItem("token");
    //om authenticated finns i localstorage och =true, navigera till tänkt route, annars navigera till
    return token ? <Outlet /> : <Navigate to="/" />
};

export default ProtectedRoute;