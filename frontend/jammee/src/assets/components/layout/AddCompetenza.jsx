import { useState } from "react";
import { Modal, Button, Form, Spinner, Alert } from "react-bootstrap";
import { useGetAllStrumentiQuery } from "../../../store/slices/api/strumentiApi";

const addCompetenza = ({ show, handleClose, onSubmit }) => {
    const [strumentoId, setStrumentoId] = useState("");
    const [voto, setVoto] = useState(1);
    const [note, setNote] = useState("");

    const { data: strumenti, error, isLoading } = useGetAllStrumentiQuery();

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit({ strumentoId, voto, note });
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Aggiungi Competenza</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                {isLoading && <Spinner animation="border" />}

                {error && <Alert variant="danger">Errore nel caricamento degli strumenti.</Alert>}

                {!isLoading && strumenti && (
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Strumento</Form.Label>
                            <Form.Select value={strumentoId} onChange={(e) => setStrumentoId(e.target.value)} required>
                                <option value="">Seleziona uno strumento...</option>
                                {strumenti.map((s) => (
                                    <option key={s.id} value={s.id}>
                                        {s.nome}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Voto (1-10)</Form.Label>
                            <Form.Control type="number" min="1" max="10" value={voto} onChange={(e) => setVoto(Number(e.target.value))} required />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Note</Form.Label>
                            <Form.Control as="textarea" rows={3} value={note} onChange={(e) => setNote(e.target.value)} />
                        </Form.Group>

                        <Button type="submit" variant="primary" className="w-100">
                            Aggiungi
                        </Button>
                    </Form>
                )}
            </Modal.Body>
        </Modal>
    );
};

export default addCompetenza;
