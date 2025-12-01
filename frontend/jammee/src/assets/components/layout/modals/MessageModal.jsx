// src/assets/components/messages/MessageModal.jsx
import { useState, useEffect } from "react";
import { Modal, Button, Form, Spinner, Alert } from "react-bootstrap";
import { useSendMessageMutation } from "../../../../store/slices/api/messaggiApi";

const MessageModal = ({ show, onClose, recipientId = null, onSent }) => {
    const [localDestinatarioId, setLocalDestinatarioId] = useState("");
    const destinatarioIdValue = recipientId ?? localDestinatarioId;

    const [contenuto, setContenuto] = useState("");
    const [sendMessage, { isLoading, isError, error }] = useSendMessageMutation();

    // Reset contenuto quando apri il modal
    useEffect(() => {
        if (show) {
            setContenuto("");
            if (!recipientId) setLocalDestinatarioId("");
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [show]);

    const handleSend = async () => {
        if (!contenuto.trim() || !destinatarioIdValue) return;
        try {
            await sendMessage({ destinatarioId: destinatarioIdValue, contenuto }).unwrap();
            onSent?.();
            onClose();
        } catch (err) {
            console.error("Errore invio messaggio", err);
        }
    };

    return (
        <Modal show={show} onHide={onClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Invia messaggio</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {isError && <Alert variant="danger">{error?.data?.message ?? "Errore invio messaggio"}</Alert>}
                <Form>
                    {/* Non mostrare l'input per il destinatario se è preimpostato */}
                    {!recipientId && (
                        <Form.Group className="mb-3">
                            <Form.Label>Destinatario (id musicista)</Form.Label>
                            <Form.Control type="text" value={localDestinatarioId} onChange={(e) => setLocalDestinatarioId(e.target.value)} placeholder="UUID destinatario" />
                            <Form.Text className="text-muted">Inserisci l'ID del musicista destinatario (UUID).</Form.Text>
                        </Form.Group>
                    )}

                    <Form.Group className="mb-3">
                        <Form.Label>Messaggio</Form.Label>
                        <Form.Control as="textarea" rows={3} value={contenuto} onChange={(e) => setContenuto(e.target.value)} placeholder="Scrivi il tuo messaggio..." />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onClose} disabled={isLoading}>
                    Annulla
                </Button>
                <Button variant="primary" onClick={handleSend} disabled={isLoading || !destinatarioIdValue || !contenuto.trim()}>
                    {isLoading ? <Spinner as="span" animation="border" size="sm" /> : "Invia"}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default MessageModal;
