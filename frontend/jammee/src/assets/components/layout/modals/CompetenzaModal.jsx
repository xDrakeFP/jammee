import { useState } from "react";
import { Modal, Button, Form, Spinner, Alert } from "react-bootstrap";
import { useGetAllStrumentiQuery } from "../../../../store/slices/api/strumentiApi";

const CompetenzaModal = ({ show, handleClose, onSubmit }) => {
    const [strumentoId, setStrumentoId] = useState("");
    const [voto, setVoto] = useState(1);
    const [note, setNote] = useState("");
    const [submitting, setSubmitting] = useState(false);

    const { data: strumenti, error, isLoading } = useGetAllStrumentiQuery();

    const strumentiFinal = Array.isArray(strumenti) ? strumenti : strumenti?.content ?? [];

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!strumentoId) return;
        try {
            setSubmitting(true);
            await onSubmit({ strumentoId, voto, note });
            setStrumentoId("");
            setVoto(1);
            setNote("");
            handleClose();
        } catch (err) {
            console.error("Errore aggiunta competenza", err);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Aggiungi Competenza</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                {isLoading && (
                    <div className="text-center">
                        <Spinner animation="border" />
                    </div>
                )}

                {error && <Alert variant="danger">Errore nel caricamento degli strumenti.</Alert>}

                {!isLoading && strumentiFinal && (
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Strumento</Form.Label>
                            <Form.Select value={strumentoId} onChange={(e) => setStrumentoId(e.target.value)} required>
                                <option value="">Seleziona uno strumento</option>
                                {strumentiFinal.map((s) => (
                                    <option key={s.id} value={s.id}>
                                        {s.nome}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Voto (1-5)</Form.Label>
                            <Form.Control type="number" min="1" max="5" value={voto} onChange={(e) => setVoto(Number(e.target.value))} required />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Note</Form.Label>
                            <Form.Control as="textarea" rows={3} value={note} onChange={(e) => setNote(e.target.value)} />
                        </Form.Group>

                        <Button type="submit" variant="primary" className="w-100" disabled={submitting || !strumentoId}>
                            {submitting ? "Salvataggio..." : "Aggiungi"}
                        </Button>
                    </Form>
                )}
            </Modal.Body>
        </Modal>
    );
};

export default CompetenzaModal;
