import {Link} from "react-router-dom";

const Navigation = () => {
    return (
        <nav>
            <Link to="/">Hem</Link> | <Link to="/login">Logga in</Link>
        </nav>
    );
};

export default Navigation;