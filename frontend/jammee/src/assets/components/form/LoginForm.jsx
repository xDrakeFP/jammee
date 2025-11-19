import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { useLoginMutation } from "../../../store/slices/api/authApi";
import { setCredentials } from "../../../store/slices/authSlice";
import { Button, Card, Form, Row } from "react-bootstrap";

const LoginForm = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [formData, setFormData] = useState({
        email: "",
        password: "",
    });

    const [login, { isLoading, error }] = useLoginMutation();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const result = await login(formData).unwrap();

            dispatch(
                setCredentials({
                    user: result.user,
                    token: result.token,
                })
            );
            navigate("/home");
        } catch (err) {
            console.error("Errore login:", err);
        }
    };

    return (
        <Form onSubmit={handleSubmit}>
            {error && <Card className="bg-red justify-content-center align-items-center d-flex">{error.data.message || "Errore nel login"}</Card>}
            <Row>
                <div className="mb-2">
                    <Form.Label>Email</Form.Label>
                    <Form.Control type="email" value={formData.email} onChange={(e) => setFormData({ ...formData, email: e.target.value })} required className="w-100 p-1" />
                </div>

                <div className="mb-2">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" value={formData.password} onChange={(e) => setFormData({ ...formData, password: e.target.value })} required className="w-100 p-1" />
                </div>

                <Button type="submit" disabled={isLoading} className="w-100 p-2 mt-3">
                    {isLoading ? <p>Caricamento... </p> : "Login"}
                </Button>

                <p className="mt-3 mx-auto">
                    Non hai un account? <a href="/register">Registrati</a>
                </p>
            </Row>
        </Form>
    );
};

export default LoginForm;
