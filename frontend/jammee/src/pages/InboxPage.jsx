// src/pages/MessagesPage.jsx
import { useEffect, useMemo, useState } from "react";
import { Button, ListGroup, Badge, Row, Col, Image, Spinner, Alert } from "react-bootstrap";
import { useGetInboxQuery, useToggleReadMutation, useDeleteMessageMutation } from "../store/slices/api/messaggiApi";
import MessageModal from "../assets/components/layout/modals/MessageModal";
const useIsMobile = (breakpoint = 768) => {
    const [isMobile, setIsMobile] = useState(typeof window !== "undefined" ? window.innerWidth < breakpoint : true);
    useEffect(() => {
        const onResize = () => setIsMobile(window.innerWidth < breakpoint);
        window.addEventListener("resize", onResize);
        return () => window.removeEventListener("resize", onResize);
    }, [breakpoint]);
    return isMobile;
};

const InboxPage = () => {
    const isMobile = useIsMobile();

    const { data: inboxPage, isLoading, isError, error, refetch } = useGetInboxQuery();
    const [toggleRead] = useToggleReadMutation();
    const [deleteMessage] = useDeleteMessageMutation();

    const [selectedId, setSelectedId] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [replyRecipient, setReplyRecipient] = useState(null);

    const messages = useMemo(() => {
        if (!inboxPage) return [];
        return Array.isArray(inboxPage) ? inboxPage : inboxPage.content ?? [];
    }, [inboxPage]);

    useEffect(() => {
        if (messages.length > 0 && !selectedId) {
            const t = setTimeout(() => {
                setSelectedId(messages[0].id);
            }, 0);
            return () => clearTimeout(t);
        }
    }, [messages, selectedId]);

    const formatDate = (iso) => {
        try {
            return new Date(iso).toLocaleString();
        } catch {
            return String(iso);
        }
    };

    const handleToggleOpen = async (msg) => {
        setSelectedId((prev) => (prev === msg.id ? null : msg.id));
        if (!msg.letto) {
            try {
                await toggleRead(msg.id).unwrap();
                refetch();
            } catch (e) {
                console.error("Errore toggle read:", e);
            }
        }
    };

    const handleDelete = async (id) => {
        if (!confirm("Sei sicuro di voler cancellare questo messaggio?")) return;
        try {
            await deleteMessage(id).unwrap();
            refetch();
            if (selectedId === id) setSelectedId(null);
        } catch (e) {
            console.error("Errore cancellazione messaggio:", e);
            alert("Impossibile cancellare il messaggio");
        }
    };

    const openReplyModal = (recipientId) => {
        setReplyRecipient(recipientId);
        setShowModal(true);
    };

    if (isLoading)
        return (
            <div className="d-flex justify-content-center mt-5">
                <Spinner />
            </div>
        );
    if (isError)
        return (
            <Alert variant="danger" className="mt-3">
                Errore caricamento messaggi: {String(error)}
            </Alert>
        );

    return (
        <Row className="mt-5">
            {isMobile ? (
                <Col>
                    <ListGroup>
                        {messages.length === 0 && <ListGroup.Item>Nessun messaggio</ListGroup.Item>}
                        {messages.map((m) => (
                            <ListGroup.Item key={m.id} action onClick={() => handleToggleOpen(m)}>
                                <div className="d-flex justify-content-between align-items-start">
                                    <div>
                                        <div className="d-flex align-items-center">
                                            <Image src={m.mittente?.avatar || "https://placecats.com/50/50"} roundedCircle width={40} height={40} className="me-2" />
                                            <div>
                                                <strong>{m.mittente?.utente?.username ?? "Unknown"}</strong>
                                                <div className="text-muted small">{formatDate(m.timestamp)}</div>
                                            </div>
                                            {!m.letto && (
                                                <Badge bg="primary" className="ms-2">
                                                    Nuovo
                                                </Badge>
                                            )}
                                        </div>
                                    </div>
                                </div>

                                {selectedId === m.id && (
                                    <div className="mt-3 text-break" style={{ whiteSpace: "pre-wrap" }}>
                                        {m.contenuto}
                                    </div>
                                )}

                                <div className="mt-2 d-flex gap-2">
                                    <Button size="sm" variant="link" onClick={() => openReplyModal(m.mittente.id)}>
                                        Rispondi
                                    </Button>
                                    <Button size="sm" variant="link" onClick={() => handleDelete(m.id)} className="text-danger">
                                        Elimina
                                    </Button>
                                </div>
                            </ListGroup.Item>
                        ))}
                    </ListGroup>

                    <MessageModal
                        show={showModal}
                        onClose={() => setShowModal(false)}
                        recipientId={replyRecipient}
                        onSent={() => {
                            refetch();
                            setShowModal(false);
                        }}
                    />
                </Col>
            ) : (
                <>
                    <Col md={4} style={{ maxHeight: "75vh", overflowY: "auto" }}>
                        <ListGroup>
                            {messages.length === 0 && <ListGroup.Item>Nessun messaggio</ListGroup.Item>}
                            {messages.map((m) => (
                                <ListGroup.Item key={m.id} action active={selectedId === m.id} onClick={() => handleToggleOpen(m)}>
                                    <div className="d-flex align-items-center">
                                        <Image src={m.mittente?.avatar || "https://placecats.com/50/50"} roundedCircle width={48} height={48} className="me-2" />
                                        <div className="flex-grow-1">
                                            <div className="d-flex justify-content-between">
                                                <strong>{m.mittente?.utente?.username ?? "Unknown"}</strong>
                                                <small className="text-muted">{formatDate(m.timestamp)}</small>
                                            </div>
                                            <div className="text-truncate" style={{ maxWidth: "220px" }}>
                                                {m.contenuto}
                                            </div>
                                        </div>
                                        {!m.letto && (
                                            <Badge bg="primary" className="ms-2">
                                                Nuovo
                                            </Badge>
                                        )}
                                    </div>
                                </ListGroup.Item>
                            ))}
                        </ListGroup>
                    </Col>

                    <Col md={8}>
                        {selectedId ? (
                            (() => {
                                const m = messages.find((x) => x.id === selectedId);
                                if (!m) return <div>Seleziona un messaggio dalla lista</div>;
                                return (
                                    <div>
                                        <div className="d-flex align-items-center mb-3">
                                            <Image src={m.mittente?.avatar || "https://placecats.com/80/80"} roundedCircle width={80} height={80} className="me-3" />
                                            <div>
                                                <h5>{m.mittente?.utente?.username ?? "Unknown"}</h5>
                                                <div className="text-muted small">{formatDate(m.timestamp)}</div>
                                            </div>
                                        </div>

                                        <div style={{ whiteSpace: "pre-wrap" }} className="mb-3">
                                            {m.contenuto}
                                        </div>

                                        <div className="d-flex gap-2 justify-content-center">
                                            <Button variant="primary" onClick={() => openReplyModal(m.mittente.id)}>
                                                Rispondi
                                            </Button>
                                            {/* <Button variant="outline-danger" onClick={() => handleDelete(m.id)}>
                                                Elimina
                                            </Button> */}
                                        </div>
                                    </div>
                                );
                            })()
                        ) : (
                            <div>Seleziona un messaggio dalla lista</div>
                        )}

                        <MessageModal
                            show={showModal}
                            onClose={() => setShowModal(false)}
                            recipientId={replyRecipient}
                            onSent={() => {
                                refetch();
                                setShowModal(false);
                            }}
                        />
                    </Col>
                </>
            )}
        </Row>
    );
};

export default InboxPage;
