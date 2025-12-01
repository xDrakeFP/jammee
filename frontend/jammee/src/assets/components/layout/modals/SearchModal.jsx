import { useMemo, useState } from "react";
import { Modal, Button, Form, Spinner } from "react-bootstrap";
import { useGetAllStrumentiQuery } from "../../../../store/slices/api/strumentiApi";
import { useGetAllGeneriQuery } from "../../../../store/slices/api/generiApi";

const SearchModal = ({ show, onClose, onSearch }) => {
    const [strumentoId, setStrumentoId] = useState("");
    const [genereId, setGenereId] = useState("");

    const { data: strumentiPage, isLoading: loadingStr } = useGetAllStrumentiQuery();
    const { data: generiPage, isLoading: loadingGen } = useGetAllGeneriQuery();

    const strumenti = useMemo(() => strumentiPage?.content ?? [], [strumentiPage]);
    const generi = useMemo(() => generiPage?.content ?? [], [generiPage]);

    const handleSubmit = () => {
        onSearch({
            strumentoId: strumentoId || null,
            genereId: genereId || null,
        });

        onClose();
    };

    return (
        <Modal show={show} onHide={onClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Cerca musicisti</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {(loadingStr || loadingGen) && <Spinner animation="border" />}
                <Form>
                    <Form.Group className="mb-3">
                        <Form.Label>Strumento</Form.Label>
                        <Form.Select value={strumentoId} onChange={(e) => setStrumentoId(e.target.value)}>
                            <option value="">— Nessuno —</option>
                            {strumenti.map((s) => (
                                <option key={s.id} value={s.id}>
                                    {s.nome}
                                </option>
                            ))}
                        </Form.Select>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Genere</Form.Label>
                        <Form.Select value={genereId} onChange={(e) => setGenereId(e.target.value)}>
                            <option value="">— Nessuno —</option>
                            {generi.map((g) => (
                                <option key={g.id} value={g.id}>
                                    {g.genere ?? g.nome}
                                </option>
                            ))}
                        </Form.Select>
                    </Form.Group>

                    <div className="d-flex justify-content-end">
                        <Button variant="secondary" onClick={onClose} className="me-2">
                            Annulla
                        </Button>
                        <Button variant="primary" onClick={handleSubmit}>
                            Cerca
                        </Button>
                    </div>
                </Form>
            </Modal.Body>
        </Modal>
    );
};

export default SearchModal;
