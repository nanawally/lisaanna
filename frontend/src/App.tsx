import ProtectedRoute from "./components/ProtectedRoute.tsx";
import Dashboard from "./components/Dashboard.tsx";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import LoginPage from "./components/LoginPage.tsx";
import RegisterUser from "./components/RegisterUser.tsx";
import Navigation from "./components/Navigation.tsx";
import "./App.css";
import UserPage from "./components/UserPage.tsx";

const App = () => {
    return (
        <>
            <BrowserRouter>
                <Navigation />
                <Routes>
                    <Route path="/" element={<LoginPage/>}/>
                    <Route element={<ProtectedRoute/>}>
                        <Route path="/dashboard" element={<Dashboard />}/>
                        <Route path="/registeruser" element={<RegisterUser />}/>
                        <Route path="/user" element={<UserPage />}/>
                    </Route>
                </Routes>
            </BrowserRouter>
        </>
    )
}

export default App;
