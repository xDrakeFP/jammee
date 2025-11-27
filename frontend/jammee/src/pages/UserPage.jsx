import { useState } from "react";
import { Container, Row, Col, Card, Image, Badge, Button, Spinner, Alert } from "react-bootstrap";
import { useGetMusicianMeQuery } from "../store/slices/api/musicistaApi";
import { useGetMusicianByIdQuery } from "../store/slices/api/musicistaApi";
import { useParams } from "react-router-dom";
import SaveLocationButton from "../assets/components/layout/SaveLocationButton";
import CompetenzaModal from "../assets/components/layout/CompetenzaModal";
import { useGetCompetenzaByMusicistaQuery, useCreateCompetenzaMutation, useDeleteCompetenzaMutation } from "../store/slices/api/competenzaApi";

const UserPage = () => {
    const { id } = useParams();

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

    const { data: competenzeData, isLoadingCompetenze } = useGetCompetenzaByMusicistaQuery(musicianId, { skip: !musicianId });

    // eslint-disable-next-line no-unused-vars
    const [createCompetenza, { isLoading: adding }] = useCreateCompetenzaMutation();
    const [deleteCompetezza, { isLoading: deleting }] = useDeleteCompetenzaMutation();

    const handleOpenModal = () => setShowModal(true);
    const handleCloseModal = () => setShowModal(false);

    const handleCompetenzaSubmit = async ({ strumentoId, voto, note }) => {
        try {
            await createCompetenza({ strumentoId, voto, note });
            handleCloseModal();
        } catch (err) {
            console.error("Errore aggiunta competenza: ", err);
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
        <Container className="py-5 bg-danger">
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
            </Row>

            <Col xs={12} className="mt-3">
                <Card className="bg-light border-0">
                    <Card.Body>
                        <div className="d-flex justify-content-between align-items-center mb-3">
                            <h5 className="text-muted mb-0">Competenze</h5>
                            {!id && (
                                <Button variant="primary" onClick={handleOpenModal}>
                                    Aggiungi competenza
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
                                            <strong>{c.strumento?.nome}</strong> — {c.voto}/5
                                            {c.note && <div className="text-muted small">{c.note}</div>}
                                        </Col>

                                        <Col className="text-end">
                                            {!id && (
                                                <Button variant="outline-danger" size="sm" onClick={() => handleDelete(c.strumento.id)} disabled={deleting}>
                                                    {deleting ? "Eliminando..." : "Elimina"}
                                                </Button>
                                            )}
                                        </Col>
                                    </Row>
                                ))}
                            </div>
                        )}
                    </Card.Body>
                </Card>
            </Col>
            <CompetenzaModal show={showModal} handleClose={handleCloseModal} onSubmit={handleCompetenzaSubmit} />
        </Container>
    );
};

export default UserPage;
