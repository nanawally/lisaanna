import {Link} from "react-router-dom";

const Navigation = () => {
    return (
        <nav>
            <Link to="/">Home</Link> | <Link to="/login">Log in</Link>
        </nav>
    );
};

export default Navigation;