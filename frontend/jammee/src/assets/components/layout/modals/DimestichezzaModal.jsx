import { useState } from "react";
import { Modal, Button, Form, Spinner, Alert } from "react-bootstrap";
import { useGetAllGeneriQuery } from "../../../../store/slices/api/generiApi";

const DimestichezzaModal = ({ show, handleClose, onSubmit }) => {
    const [genereId, setGenereId] = useState("");
    const [voto, setVoto] = useState(1);
    const [note, setNote] = useState("");
    const [submitting, setSubmitting] = useState(false);

    const { data: generi, error, isLoading } = useGetAllGeneriQuery();

    const generiFinal = Array.isArray(generi) ? generi : generi?.content ?? [];

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!genereId) return;
        try {
            setSubmitting(true);
            await onSubmit({ genereId, voto, note });
            setGenereId("");
            setVoto(1);
            setNote("");
            handleClose();
        } catch (err) {
            console.error("Errore aggiunta dimestichezza", err);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Aggiungi Dimestichezza</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                {isLoading && (
                    <div className="text-center">
                        <Spinner animation="border" />
                    </div>
                )}

                {error && <Alert variant="danger">Errore nel caricamento dei generi.</Alert>}

                {!isLoading && generiFinal && (
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Genere</Form.Label>
                            <Form.Select value={genereId} onChange={(e) => setGenereId(e.target.value)} required>
                                <option value="">Seleziona un genere</option>
                                {generiFinal.map((g) => (
                                    <option key={g.id} value={g.id}>
                                        {g.genere}
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

                        <Button type="submit" variant="primary" className="w-100" disabled={submitting || !genereId}>
                            {submitting ? "Salvataggio..." : "Aggiungi"}
                        </Button>
                    </Form>
                )}
            </Modal.Body>
        </Modal>
    );
};

export default DimestichezzaModal;
