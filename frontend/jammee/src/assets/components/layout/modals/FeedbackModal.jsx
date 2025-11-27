// components/FeedbackModal.jsx
import { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const FeedbackModal = ({ show, handleClose, onSubmit }) => {
    const [voto, setVoto] = useState(5);
    const [note, setNote] = useState("");

    const handleSave = () => {
        if (!voto) return;
        onSubmit({ voto: Number(voto), note });
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Lascia un feedback</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-3">
                        <Form.Label>Voto (1-5)</Form.Label>
                        <Form.Control type="number" min={1} max={5} value={voto} onChange={(e) => setVoto(e.target.value)} />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Note (opzionali)</Form.Label>
                        <Form.Control as="textarea" rows={4} value={note} onChange={(e) => setNote(e.target.value)} />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Annulla
                </Button>
                <Button variant="primary" onClick={handleSave}>
                    Invia
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default FeedbackModal;
