import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { setCredentials } from "../../../store/slices/authSlice";
import { Form, Card, Row, Button } from "react-bootstrap";
import { useRegisterMusicianMutation } from "../../../store/slices/api/authApi";

export const MusicianForm = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const { token, user } = useSelector((state) => state.auth);
    console.log("Stato auth in MusicianForm:", { token, user });

    const [formData, setFormData] = useState({
        avatar: "",
        bio: "",
        indirizzo: "",
        canHost: false,
    });

    const [register, { isLoading, error }] = useRegisterMusicianMutation();

    const handleSubmit = async (e) => {
        e.preventDefault();

        console.log("Invio i dati del musicista:", formData);

        try {
            const result = await register({
                avatar: formData.avatar,
                bio: formData.bio,
                indirizzo: formData.indirizzo,
                canHost: formData.canHost,
            }).unwrap();

            dispatch(
                setCredentials({
                    user: result.user,
                    token: result.token,
                })
            );

            navigate("/home");
        } catch (err) {
            console.error("Errore registrazione musicista:", err);
        }
    };

    return (
        <Form onSubmit={handleSubmit}>
            {error && <Card className="bg-red justify-content-center align-items-center d-flex">{error.data.message || "Errore nella registrazione del musicista"}</Card>}
            <Row>
                <div className="mb-2">
                    <Form.Label>Avatar URL</Form.Label>
                    <Form.Control type="text" value={formData.avatar} onChange={(e) => setFormData({ ...formData, avatar: e.target.value })} required className="w-100 p-1" />
                </div>

                <div className="mb-2">
                    <Form.Label>Bio</Form.Label>
                    <Form.Control as="textarea" rows={3} value={formData.bio} onChange={(e) => setFormData({ ...formData, bio: e.target.value })} required className="w-100 p-1" />
                </div>

                <div className="mb-2">
                    <Form.Label>Indirizzo</Form.Label>
                    <Form.Control type="text" value={formData.indirizzo} onChange={(e) => setFormData({ ...formData, indirizzo: e.target.value })} required className="w-100 p-1" />
                </div>
                <div className="mb-2">
                    <Form.Check type="checkbox" label="Puoi ospitare eventi a casa tua?" checked={formData.canHost} onChange={(e) => setFormData({ ...formData, canHost: e.target.checked })} />
                </div>
                <div className="d-flex justify-content-center mt-3">
                    <Button variant="primary" type="submit" disabled={isLoading}>
                        {isLoading ? "Registrazione in corso..." : "Registrati come Musicista"}
                    </Button>
                </div>
            </Row>
        </Form>
    );
};
