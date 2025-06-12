import {useEffect, useState} from "react";

interface AppUser {
    id: number;
    username: string;
    password: string;
    role: string;
    consentGiven: boolean;
}

const UserPage: React.FC = () => {
    const [users, setUsers] = useState<AppUser[]>([]);
    const [searchUsername, setSearchUsername] = useState('');
    const [searchedUser, setSearchedUser] = useState<AppUser | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [searchError, setSearchError] = useState<string | null>(null);

    useEffect(() => {
        fetchUsers();
    }, []);

    const [message, setMessage] = useState("");

    const fetchUsers = async () => {
        setLoading(true);
        setError(null);
        const token = localStorage.getItem("token");
        if (!token) {
            setMessage("No valid token found");
            return;
        }
        try {
            const response = await fetch("http://localhost:8080/user",
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });
            if (!response.ok) {
                throw new Error(`Error fetching users: ${response.statusText}`);
            }
            const data: AppUser[] = await response.json();
            setUsers(data);
        } catch (err) {
            if (err instanceof Error) {
                setError(err.message);
            }
        } finally {
            setLoading(false);
        }
    };

    const findUserByUsername = async () => {
        if (!searchUsername.trim()) {
            setSearchError('Please enter a username.');
            setSearchedUser(null);
            return;
        }

        setSearchError(null);
        try {
            const response = await fetch(`/user/${encodeURIComponent(searchUsername)}`);
            if (response.status === 404) {
                setSearchedUser(null);
                setSearchError('User not found.');
                return;
            }
            if (!response.ok) {
                throw new Error(`Error fetching user: ${response.statusText}`);
            }
            const user: AppUser = await response.json();
            setSearchedUser(user);
        } catch (err) {
            if (err instanceof Error) {
                setSearchError(err.message);
                setSearchedUser(null);
            }
        }
    };

    return (
        <>
            {
                message && <div>
                    {message}
                </div>
            }
            <div style={{padding: '20px', maxWidth: '600px', margin: '0 auto'}}>
            <h2 style={{marginBottom: '20px'}}>User List</h2>

            {/* Load all users */}
            <button onClick={fetchUsers} style={{marginBottom: '20px'}}>Refresh User List</button>

            {loading && <p>Loading users...</p>}
            {error && <p style={{color: 'red'}}>Error: {error}</p>}

            {/* Display all users */}
            <div style={{display: 'flex', flexDirection: 'column', gap: '10px'}}>
                {users.map(user => (
                    <div key={user.id} style={{
                        padding: '10px',
                        border: '1px solid #ccc',
                        borderRadius: '4px',
                        backgroundColor: '#f9f9f9'
                    }}>
                        <p><strong>Username:</strong> {user.username}</p>
                        <p><strong>Role:</strong> {user.role}</p>
                    </div>
                ))}
            </div>

            {/* Search for a user by username */}
            <div style={{marginTop: '30px'}}>
                <h3>Find User by Username</h3>
                <input
                    type="text"
                    placeholder="Enter username"
                    value={searchUsername}
                    onChange={(e) => setSearchUsername(e.target.value)}
                    style={{marginRight: '10px'}}
                />
                <button onClick={findUserByUsername}>Find User</button>
                {searchError && <p style={{color: 'red', marginTop: '10px'}}>{searchError}</p>}

                {/* Display searched user */}
                {searchedUser && (
                    <div style={{
                        marginTop: '20px',
                        padding: '10px',
                        border: '1px solid #ccc',
                        borderRadius: '4px',
                        backgroundColor: '#f0f0f0'
                    }}>
                        <h4>User Details:</h4>
                        <p><strong>ID:</strong> {searchedUser.id}</p>
                        <p><strong>Username:</strong> {searchedUser.username}</p>
                        <p><strong>Role:</strong> {searchedUser.role}</p>
                        {/* add other fields if needed */}
                    </div>
                )}
            </div>
        </div></>

    );
};

export default UserPage;