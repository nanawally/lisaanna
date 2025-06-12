import {useForm} from "react-hook-form";
import {useState} from "react";

type UserFormValues = {
    username: string;
    password: string;
    role: string;
    consentGiven: boolean;
};


const RegisterUser = () => {
    const {
        register,
        handleSubmit,
        formState: {errors},
        reset,
    } = useForm<UserFormValues>({
        defaultValues: {
            consentGiven: false
        }
    });

    const [message, setMessage] = useState("");

    const handleRegisterUser = async (data: UserFormValues) => {
        if (!data.consentGiven) {
            setMessage("You must consent to GDPR to continue.");
            return;
        }

        const token = localStorage.getItem("token");
        if (!token) {
            setMessage("No valid token found");
            return;
        }
        try {
            const response = await fetch("http://localhost:8080/user",
                {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data),
                }
            );
            if (!response.ok) {
                throw new Error("Unable to register user.");
            }
            setMessage("User has been registered.");
            reset();
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
            <form onSubmit={handleSubmit(handleRegisterUser)}>
                <div>
                    <input type="text"
                           placeholder="Username" {...register("username", {required: "Username is mandatory"})}/>
                    {errors.username && <p>{errors.username.message}</p>}
                </div>
                <div>
                    <input type="password"
                           placeholder="Password" {...register("password", {required: "Password is mandatory"})}/>
                    {errors.password && <p>{errors.password.message}</p>}
                </div>
                <div>
                    <input type="text"
                           placeholder="Role" {...register("role", {required: "Role is mandatory"})}/>
                    {errors.role && <p>{errors.role.message}</p>}
                </div>
                <div>
                    <input type="checkbox" id="consentGiven" {...register("consentGiven")} />
                    <label htmlFor="consentGiven">Do you consent to GDPR?</label>
                    {errors.consentGiven && <p>{errors.consentGiven.message}</p>}
                </div>
                <button type="submit">Register user</button>
            </form>
        </>
    );

}

export default RegisterUser;