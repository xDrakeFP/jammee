import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { setCredentials } from "../../../store/slices/authSlice";
import { useRegisterMutation, useLoginMutation } from "../../../store/slices/api/authApi";
import { Form, Card, Row, Button } from "react-bootstrap";

export const RegisterForm = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [formData, setFormData] = useState({
        username: "",
        nome: "",
        cognome: "",
        email: "",
        password: "",
        telefono: "",
        dataNascita: "",
    });

    const [register, { isLoading, error }] = useRegisterMutation();
    const [login] = useLoginMutation();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const result = await register({
                username: formData.username,
                nome: formData.nome,
                cognome: formData.cognome,
                email: formData.email,
                password: formData.password,
                telefono: formData.telefono,
                dataNascita: formData.dataNascita,
            }).unwrap();

            const loginRes = await login({ email: formData.email, password: formData.password }).unwrap();
            console.log("Login response:", loginRes);

            dispatch(
                setCredentials({
                    user: result || null,
                    token: loginRes.accessToken,
                })
            );

            localStorage.setItem("user", JSON.stringify(result));
            localStorage.setItem("accessToken", loginRes.accessToken);

            navigate("/musician/register");
        } catch (err) {
            console.error("Errore registrazione:", err);
        }
    };

    return (
        <Form onSubmit={handleSubmit}>
            {error && <Card className="bg-red justify-content-center align-items-center d-flex">{error.data.message || "Errore nella registrazione"}</Card>}
            <Row>
                <div className="mb-2">
                    <Form.Label>Username</Form.Label>
                    <Form.Control type="text" value={formData.username} onChange={(e) => setFormData({ ...formData, username: e.target.value })} required className="w-100 p-1" />
                </div>

                <div className="mb-2">
                    <Form.Label>Nome</Form.Label>
                    <Form.Control type="text" value={formData.nome} onChange={(e) => setFormData({ ...formData, nome: e.target.value })} required className="w-100 p-1" />
                </div>

                <div className="mb-2">
                    <Form.Label>Cognome</Form.Label>
                    <Form.Control type="text" value={formData.cognome} onChange={(e) => setFormData({ ...formData, cognome: e.target.value })} required className="w-100 p-1" />
                </div>

                <div className="mb-2">
                    <Form.Label>Email</Form.Label>
                    <Form.Control type="email" value={formData.email} onChange={(e) => setFormData({ ...formData, email: e.target.value })} required className="w-100 p-1" />
                </div>

                <div className="mb-2">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" value={formData.password} onChange={(e) => setFormData({ ...formData, password: e.target.value })} required className="w-100 p-1" />
                </div>

                <div className="mb-2">
                    <Form.Label>Telefono</Form.Label>
                    <Form.Control type="tel" value={formData.telefono} onChange={(e) => setFormData({ ...formData, telefono: e.target.value })} required className="w-100 p-1" />
                </div>

                <div className="mb-2">
                    <Form.Label>Data di nascita</Form.Label>
                    <Form.Control type="date" value={formData.dataNascita} onChange={(e) => setFormData({ ...formData, dataNascita: e.target.value })} required className="w-100 p-1" />
                </div>

                <Button type="submit" disabled={isLoading} className="w-100 p-1 align-items-center d-flex justify-content-center mt-3 p-2">
                    {isLoading ? <p>Caricamento... </p> : "Procedi"}
                </Button>

                <p className="mt-3 mx-auto">
                    Hai già un account? <a href="/login">Login</a>
                </p>
            </Row>
        </Form>
    );
};
