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
    const [userId, setUserId] = useState<string>("");

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
        const token = localStorage.getItem("token");
        if (!token) {
            setSearchError("No valid token found.");
            setSearchedUser(null);
            return;
        }
        try {
            const response = await fetch(`http://localhost:8080/user/${encodeURIComponent(searchUsername)}`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            if (response.status === 404) {
                setSearchedUser(null);
                setSearchError("User not found.");
                return;
            }
            if (!response.ok) {
                throw new Error("Error fetching user.");
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

    const handleDeleteUser = async () => {
        const token = localStorage.getItem("token");
        if (!token) {
            setMessage("No valid token found");
            return;
        }
        if (!userId.trim()) {
            setMessage("Please enter a valid user ID.")
            return;
        }
        try {
            const response = await fetch(`http://localhost:8080/user/${userId}`,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            if (!response.ok) {
                throw new Error("Unable to delete user.");
            }
            setMessage("User has been deleted.");
        } catch (err) {
            if (err instanceof Error) {
                setMessage(err.message);
            }
        }
    }

    return (
        <>
            {message && (
                <div style={{
                    marginBottom: '20px',
                    padding: '10px',
                    border: '1px solid green',
                    borderRadius: '4px',
                    backgroundColor: '#e0ffe0',
                    color: 'green'
                }}>
                    {message}
                </div>
            )}
            <div style={{padding: '20px', maxWidth: '600px', margin: '0 auto'}}>
                <h2 style={{marginBottom: '20px', color: '#f9f9f9'}}>User List</h2>

                <button onClick={fetchUsers} style={{marginBottom: '20px', color: '#f9f9f9'}}>Refresh User List</button>

                {loading && <p>Loading users...</p>}
                {error && <p style={{color: 'red'}}>Error: {error}</p>}

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

                <div style={{marginTop: '30px'}}>
                    <h3 style={{color: '#f9f9f9'}}>Find User by Username</h3>
                    <input
                        type="text"
                        placeholder="Enter username"
                        value={searchUsername}
                        onChange={(e) => setSearchUsername(e.target.value)}
                        style={{marginRight: '10px'}}
                    />
                    <button onClick={findUserByUsername}>Find User</button>
                    {searchError && <p style={{color: 'red', marginTop: '10px'}}>{searchError}</p>}

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
                        </div>
                    )}
                </div>
                <div style={{marginTop: '30px'}}>
                    <h3 style={{color: '#f9f9f9'}}>Delete user by ID</h3>
                    <input
                        type="number"
                        placeholder="Enter ID"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        style={{marginRight: '10px'}}
                    />
                    <button onClick={handleDeleteUser}>Delete User</button>
                </div>
            </div>
        </>

    );
};

export default UserPage;