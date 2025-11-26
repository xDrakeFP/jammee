import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux";
import { Alert, Badge, Button, Form, ListGroup, Spinner } from "react-bootstrap";
import { getMyLocation, getNearbyUsers, setMaxKm, setPageNumber } from "../store/actions/PosizioneAction";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { myPosition, nearbyUsers, loading, maxKm, pageNumber, pageSize /* totalElements*/ } = useSelector((state) => state.location);
    const [positionNotFound, setPositionNotFound] = useState(false);
    const locationState = useSelector((state) => state.location);
    console.log("Redux locationState:", locationState);

    useEffect(() => {
        const fetchMyLocation = async () => {
            try {
                const res = await dispatch(getMyLocation());
                console.log("Mia posizione ottenuta:", res);
                setPositionNotFound(false);
            } catch (err) {
                if (err?.status === 404) {
                    console.warn("Posizione non trovata per l'utente.", err);
                    setPositionNotFound(true);
                }
            }
        };
        fetchMyLocation();
    }, [dispatch]);

    useEffect(() => {
        if (myPosition?.latitudine && myPosition?.longitudine) {
            dispatch(
                getNearbyUsers({
                    lat: myPosition.latitudine,
                    lng: myPosition.longitudine,
                    maxKm,
                    pageNumber,
                    pageSize,
                    sortBy: "distance",
                })
            )
                .unwrap?.()
                .then((res) => console.log("Utenti vicini ottenuti:", res))
                .catch((err) => console.error("Errore ottenendo utenti vicini:", err));
        }
    }, [dispatch, myPosition, maxKm, pageNumber, pageSize]);

    const handleDistanceChange = (e) => {
        const value = Number(e.target.value);
        dispatch(setMaxKm(value));
        dispatch(setPageNumber(0));
    };

    // const handlePrevPage = () => {
    //     if (pageNumber > 0) {
    //         dispatch(setPageNumber(pageNumber - 1));
    //     }
    // };

    // const handleNextPage = () => {
    //     if ((pageNumber + 1) * pageSize < totalElements) {
    //         dispatch(setPageNumber(pageNumber + 1));
    //     }
    // };

    return (
        <div className="container mt-5">
            <h1>Utenti vicino a te</h1>
            <div className="mb-3 d-flex align-items-center">
                <span className="me-2">Range:</span>
                <Form.Control type="number" value={maxKm} onChange={handleDistanceChange} className="me-2" size="sm" />
                <span className="align-self-center">km</span>
            </div>

            {loading && <Spinner animation="border" />}

            {positionNotFound && (
                <Alert variant="warning" className="d-flex justify-content-between align-items-center">
                    Posizione non individuata, per usufruire delle funzionalità di localizzazione di Jammee salvare la posizione.
                    <Button variant="primary" onClick={() => navigate("/profile")}>
                        Vai al profilo
                    </Button>
                </Alert>
            )}

            {!loading && nearbyUsers?.length === 0 && <Alert variant="info">Nessun utente trovato entro {maxKm} km.</Alert>}

            <ListGroup className="mt-2">
                {nearbyUsers?.map((pos) => {
                    const musicista = pos.posizione.musicista;
                    const utente = musicista.utente;

                    return (
                        <ListGroup.Item key={musicista.id} className="d-flex justify-content-between align-items-center">
                            <div>{`${utente.username} (${utente.nome})`}</div>
                            <div>
                                {pos.distanza != null && (
                                    <Badge bg="primary" pill>
                                        {pos.distanza.toFixed(1)} km
                                    </Badge>
                                )}
                            </div>
                        </ListGroup.Item>
                    );
                })}
            </ListGroup>
            {/*
            <div className="mt-3 d-flex justify-content-between">
                <Button variant="secondary" onClick={handlePrevPage} disabled={pageNumber === 0}>
                    Precedente
                </Button>
                <Button variant="secondary" onClick={handleNextPage} disabled={(pageNumber + 1) * pageSize >= totalElements}>
                    Successiva
                </Button>
            </div>
             */}
        </div>
    );
};

export default HomePage;
