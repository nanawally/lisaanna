import ProtectedRoute from "./components/ProtectedRoute.tsx";
import Dashboard from "./components/Dashboard.tsx";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import LoginPage from "./components/LoginPage.tsx";
import RegisterUser from "./components/RegisterUser.tsx";

const App = () => {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<LoginPage/>}/>
                    <Route element={<ProtectedRoute/>}>
                        <Route path="/dashboard" element={<Dashboard/>}/>
                        <Route path="/registeruser" element={<RegisterUser/>}/>
                    </Route>
                </Routes>
            </BrowserRouter>
        </>
    )
}

export default App;
