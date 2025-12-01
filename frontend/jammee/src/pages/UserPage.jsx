import { useState } from "react";
import { Container, Row, Col, Card, Image, Badge, Button, Spinner, Alert } from "react-bootstrap";
import { useGetMusicianMeQuery } from "../store/slices/api/musicistaApi";
import { useGetMusicianByIdQuery } from "../store/slices/api/musicistaApi";
import { useNavigate, useParams } from "react-router-dom";
import SaveLocationButton from "../assets/components/layout/SaveLocationButton";
import CompetenzaModal from "../assets/components/layout/modals/CompetenzaModal";
import DimestichezzaModal from "../assets/components/layout/modals/DimestichezzaModal";
import FeedbackModal from "../assets/components/layout/modals/FeedbackModal";
import MessageModal from "../assets/components/layout/modals/MessageModal";
import { useGetCompetenzaByMusicistaQuery, useCreateCompetenzaMutation, useDeleteCompetenzaMutation } from "../store/slices/api/competenzaApi";
import { useCreateDimestichezzaMutation, useGetDimestichezzaByMusicistaQuery } from "../store/slices/api/dimestichezzaApi";
import { useCreateFeedbackMutation, useGetFeedbackByRecipientQuery, useDeleteFeedbackMutation } from "../store/slices/api/feedbackApi";

const UserPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const {
        data: myMusician,
        isLoading: isLoadingMy,
        error: errorMy,
    } = useGetMusicianMeQuery(undefined, {
        skip: !!id,
    });

    const {
        data: otherMusician,
        isLoading: isLoadingOther,
        error: errorOther,
    } = useGetMusicianByIdQuery(id, {
        skip: !id,
    });

    const musician = id ? otherMusician : myMusician;
    const musicianId = musician?.utente?.id;
    const isLoading = id ? isLoadingOther : isLoadingMy;
    const error = id ? errorOther : errorMy;
    const [showModal, setShowModal] = useState(false);
    const [showModalD, setShowModalD] = useState(false);
    const [showFeedbackModal, setShowFeedbackModal] = useState(false);
    const isOwnProfile = myMusician && musicianId === myMusician?.utente.id;

    const { data: competenzeData, isLoadingCompetenze } = useGetCompetenzaByMusicistaQuery(musicianId, { skip: !musicianId });
    const { data: dimestichezzeData, isLoadingDimestichezze } = useGetDimestichezzaByMusicistaQuery(musicianId, { skip: !musicianId });
    console.log("musicianId prima di get all feedback", musicianId);
    const { data: feedbackData, isLoadingFeedback, error: feedbackError } = useGetFeedbackByRecipientQuery(musicianId, { skip: !musicianId });

    const [showMessageModal, setShowMessageModal] = useState(false);
    const [replyRecipient, setReplyRecipient] = useState(null);

    // eslint-disable-next-line no-unused-vars
    const [createCompetenza, { isLoading: adding }] = useCreateCompetenzaMutation();
    const [deleteCompetezza, { isLoading: deleting }] = useDeleteCompetenzaMutation();

    // eslint-disable-next-line no-unused-vars
    const [createDimestichezza, { isLoading: addingD }] = useCreateDimestichezzaMutation();
    const [deleteDimestichezza, { isLoading: deletingD }] = useDeleteCompetenzaMutation();

    // eslint-disable-next-line no-unused-vars
    const [createFeedback, { isLoading: addingF }] = useCreateFeedbackMutation();
    const [deleteFeedback, { isLoading: deletingF }] = useDeleteFeedbackMutation();

    const handleOpenModal = () => setShowModal(true);
    const handleCloseModal = () => setShowModal(false);

    const handleOpenModalD = () => setShowModalD(true);
    const handleCloseModalD = () => setShowModalD(false);

    const handleOpenFeedback = () => setShowFeedbackModal(true);
    const handleCloseFeedback = () => setShowFeedbackModal(false);

    const handleOpenMessageModal = () => {
        setReplyRecipient(musician?.id);
        setShowMessageModal(true);
    };

    const handleCloseMessageModal = () => setShowMessageModal(false);

    const handleCompetenzaSubmit = async ({ strumentoId, voto, note }) => {
        try {
            await createCompetenza({ strumentoId, voto, note });
            handleCloseModal();
        } catch (err) {
            console.error("Errore aggiunta competenza: ", err);
        }
    };

    const handleDimestichezzaSubmit = async ({ genereId, voto, note }) => {
        try {
            await createDimestichezza({ genereId, voto, note });
            handleCloseModalD();
        } catch (err) {
            console.error("Errore aggiunta dimestichezza: ", err);
        }
    };

    const handleFeedbackSubmit = async ({ voto, note }) => {
        try {
            await createFeedback({ voto, note, destinatarioId: musician.id }).unwrap;
            handleCloseFeedback();
        } catch (err) {
            console.error("Errore invio feedback", err);
        }
    };

    const handleDelete = async (strumentoId) => {
        if (!window.confirm("Sei sicuro di voler eliminare questa competenza?")) return;
        try {
            await deleteCompetezza(strumentoId).unwrap();
        } catch (err) {
            console.error("Errore nella cancellazione della competenza", err);
        }
    };

    const handleDeleteDimestichezza = async (genereId) => {
        if (!window.confirm("Sei sicuro di voler eliminare questa competenza?")) return;
        try {
            await deleteDimestichezza(genereId).unwrap();
        } catch (err) {
            console.error("Errore nella eliminazione", err);
        }
    };

    const handleDeleteFeedback = async (feedbackId) => {
        if (!window.confirm("Sei sicuro di voler eliminare questo feedback?")) return;
        try {
            await deleteFeedback(feedbackId).unwrap();
        } catch (err) {
            console.error("Errore nella cancellazione del feedback", err);
        }
    };

    if (isLoading) {
        return (
            <Container className="mt-5">
                <div className="text-center">
                    <div className="spinner-border text-primary" role="status">
                        <span className="visually-hidden">Caricamento...</span>
                    </div>
                </div>
            </Container>
        );
    }

    if (error || !musician) {
        return (
            <Container className="mt-5">
                <Card className="text-center border-danger">
                    <Card.Body>
                        <Card.Text className="text-danger">Errore nel caricamento del profilo</Card.Text>
                    </Card.Body>
                </Card>
            </Container>
        );
    }

    const avatarUrl = musician?.avatar?.trim();

    return (
        <Container className="py-5 bg-danger mt-5">
            <Row className="justify-content-center mt-3">
                <Col xs={12} md={10} lg={8}>
                    <Card className="shadow-sm border-0">
                        <Card.Body className="p-4 p-md-5">
                            <Row className="mb-4">
                                <Col xs={12} className="text-center">
                                    <Image src={avatarUrl} roundedCircle className="mb-3 border border-3" width={150} height={150} />
                                    <h2 className="fw-bold mb-1">{musician?.utente?.username}</h2>
                                </Col>
                            </Row>

                            <Row className="g-4">
                                <Col xs={12}>
                                    <Card className="bg-light border-0">
                                        <Card.Body>
                                            <h5 className="text-muted mb-3">Informazioni Personali</h5>
                                            <Row className="mb-2">
                                                <Col xs={4} className="fw-semibold">
                                                    Nome:
                                                </Col>
                                                <Col xs={8}>
                                                    {musician?.utente?.nome} {musician?.utente?.cognome}
                                                </Col>
                                            </Row>
                                            <Row className="mb-2">
                                                <Col xs={4} className="fw-semibold">
                                                    Email:
                                                </Col>
                                                <Col xs={8} className="text-break">
                                                    {musician?.utente.email}
                                                </Col>
                                            </Row>
                                        </Card.Body>
                                    </Card>
                                </Col>

                                {musician?.bio && (
                                    <Col xs={12}>
                                        <Card className="bg-light border-0">
                                            <Card.Body>
                                                <h5 className="text-muted mb-3">Bio</h5>
                                                <p className="mb-0">{musician?.bio}</p>
                                            </Card.Body>
                                        </Card>
                                    </Col>
                                )}

                                <Col xs={12}>
                                    <Card className="bg-light border-0">
                                        <Card.Body>
                                            <Row className="align-items-center">
                                                <Col xs={12} sm={6}>
                                                    <small className="text-muted d-block">Indirizzo</small>
                                                    <span className="text-secondary">{musician?.indirizzo}</span>
                                                </Col>
                                                <Col xs={12} sm={6} className="text-sm-end mt-2 mt-sm-0">
                                                    {musician?.canHost && (
                                                        <Badge bg="success" className="px-3 py-2">
                                                            <i className="bi bi-house-check me-2"></i>
                                                            Può ospitare eventi
                                                        </Badge>
                                                    )}
                                                </Col>
                                            </Row>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            </Row>
                            <Row className="mt-4">
                                <Col xs={12}>
                                    <SaveLocationButton />
                                </Col>
                            </Row>
                            <Row className="mt-4">
                                <Col xs={12} className="text-center">
                                    <small className="text-muted">Membro dal {new Date(musician?.utente.dataRegistrazione).toLocaleDateString("it-IT")}</small>
                                </Col>
                            </Row>
                        </Card.Body>
                    </Card>
                </Col>
                <Col xs={12} lg={4} className="mt-3">
                    <Card className="bg-light border-0">
                        <Card.Body>
                            <div className="d-flex justify-content-between align-items-center mb-3">
                                <h5 className="text-muted mb-3 mx-auto">Competenza</h5>
                                {!id && (
                                    <Button variant="primary" onClick={handleOpenModal}>
                                        <i className="bi bi-plus"></i>
                                    </Button>
                                )}
                            </div>

                            {isLoadingCompetenze && (
                                <div className="text-center">
                                    <Spinner animation="border" />
                                </div>
                            )}

                            {!isLoadingCompetenze && (!competenzeData || competenzeData.content.length === 0) && <Alert variant="info">Nessuna competenza aggiunta.</Alert>}

                            {!isLoadingCompetenze && competenzeData && competenzeData.content.length > 0 && (
                                <div>
                                    {competenzeData.content.map((c) => (
                                        <Row key={c.id} className="d-flex align-items-center mb-3 ">
                                            <Col className="text-start">
                                                <strong>{c.strumento?.nome}</strong>
                                                <p className="mb-1">{c.voto}/5</p>
                                                {c.note && <div className="text-muted small">{c.note}</div>}
                                            </Col>

                                            <Col className="text-end">
                                                {!id && (
                                                    <Button variant="outline-danger" size="sm" onClick={() => handleDelete(c.strumento.id)} disabled={deleting}>
                                                        <i className="bi bi-trash"></i>
                                                    </Button>
                                                )}
                                            </Col>
                                        </Row>
                                    ))}
                                </div>
                            )}
                        </Card.Body>
                    </Card>

                    <Card className="bg-light border-0 mt-3">
                        <Card.Body>
                            <div className="d-flex justify-content-between align-items-center mb-3">
                                <h5 className="text-muted mb-3 mx-auto">Dimestichezza</h5>
                                {!id && (
                                    <Button variant="primary" onClick={handleOpenModalD}>
                                        <i className="bi bi-plus"></i>
                                    </Button>
                                )}
                            </div>

                            {isLoadingDimestichezze && (
                                <div className="text-center">
                                    <Spinner animation="border" />
                                </div>
                            )}

                            {!isLoadingDimestichezze && (!dimestichezzeData || dimestichezzeData.content.length === 0) && <Alert variant="info">Nessuna dimestichezza aggiunta.</Alert>}

                            {!isLoadingDimestichezze && dimestichezzeData && dimestichezzeData.content.length > 0 && (
                                <div>
                                    {dimestichezzeData.content.map((d) => (
                                        <Row key={d.id} className="d-flex align-items-center mb-3">
                                            <Col className="text-start">
                                                <strong>{d.genere?.genere}</strong>
                                                <p className="mb-2">{d.voto}/5</p>
                                                {d.note && <div className="text-muted small">{d.note}</div>}
                                            </Col>
                                            <Col className="text-end">
                                                {!id && (
                                                    <Button variant="outline-danger" size="sm" onClick={() => handleDeleteDimestichezza(d.id)} disabled={deletingD}>
                                                        <i className="bi bi-trash"></i>
                                                    </Button>
                                                )}
                                            </Col>
                                        </Row>
                                    ))}
                                </div>
                            )}
                        </Card.Body>
                    </Card>

                    <Card className="bg-light border-0 mt-3">
                        <Card.Body>
                            <h5 className="text-muted mb-3">Feedback</h5>

                            {isLoadingFeedback && (
                                <div className="text-center">
                                    <Spinner animation="border" />
                                </div>
                            )}
                            {console.log("feedbackError :", feedbackError, feedbackData)}
                            {feedbackError && <Alert variant="danger">Errore caricamento feedback.</Alert>}

                            {!isLoadingFeedback && (!feedbackData || feedbackData.content.length === 0) && <Alert variant="info">Nessun feedback per questo utente.</Alert>}

                            {!isLoadingFeedback && feedbackData && feedbackData.content.length > 0 && (
                                <div className="feedback-list mt-2">
                                    {feedbackData.content.map((f) => (
                                        <div key={f.id} onClick={() => navigate(`/musician/${f.mittente?.id}`)} className="feedback-item d-flex justify-content-between align-items-start mb-3">
                                            <div className="m-auto">
                                                <div className="my-2">
                                                    <Image
                                                        className="me-2"
                                                        src={f.mittente?.utente?.avatar || "https://placecats.com/100/100"}
                                                        alt="Profile"
                                                        roundedCircle
                                                        width={50}
                                                        height={50}
                                                        style={{ objectFit: "cover" }}
                                                    />
                                                    <strong>{f.mittente?.utente?.username ?? "Utente"}</strong>
                                                </div>
                                                <div className="small text-muted"> {new Date(f.timestamp).toLocaleString()}</div>
                                                <div className="mt-1">{f.note}</div>
                                                <div className="small text-secondary mt-1">Voto: {f.voto}/5</div>
                                            </div>

                                            {myMusician?.utente?.id === f.mittente?.utente?.id && (
                                                <Button variant="outline-danger" size="sm" onClick={() => handleDeleteFeedback(f.id).unwrap()} disabled={deletingF}>
                                                    <i className="bi bi-trash"></i>
                                                </Button>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            )}
                        </Card.Body>
                    </Card>

                    {!isOwnProfile && (
                        <div className="d-flex justify-content-end mb-2">
                            <Button variant="primary" size="sm" onClick={handleOpenFeedback} className="mx-auto mt-1">
                                Lascia un feedback
                            </Button>
                        </div>
                    )}

                    {!isOwnProfile && (
                        <div className="d-flex justify-content-center mb-3">
                            <Button variant="success" onClick={handleOpenMessageModal}>
                                Invia Messaggio
                            </Button>
                        </div>
                    )}
                </Col>
            </Row>
            <CompetenzaModal show={showModal} handleClose={handleCloseModal} onSubmit={handleCompetenzaSubmit} />
            <DimestichezzaModal show={showModalD} handleClose={handleCloseModalD} onSubmit={handleDimestichezzaSubmit} />
            <FeedbackModal show={showFeedbackModal} handleClose={handleCloseFeedback} onSubmit={handleFeedbackSubmit} />
            <MessageModal
                show={showMessageModal}
                onClose={handleCloseMessageModal}
                recipientId={replyRecipient}
                onSent={() => {
                    setShowMessageModal(false);
                }}
            />
        </Container>
    );
};

export default UserPage;
