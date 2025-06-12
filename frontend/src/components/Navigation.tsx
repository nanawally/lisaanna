import {Link} from "react-router-dom";

const Navigation = () => {
    return (
        <nav>
            <Link to="/">Home</Link>
            <Link to="/login">Log in</Link>
            <Link to="/user">Users</Link>
            <Link to="/registeruser">Register User</Link>
        </nav>
    );
};

export default Navigation;