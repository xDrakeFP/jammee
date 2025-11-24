import { useEffect, useState, useRef } from "react";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux";
import { Alert, Badge, Button, Form, ListGroup, Spinner } from "react-bootstrap";
import { getMyLocation, getNearbyUsers, setMaxKm, setPageNumber } from "../store/actions/PosizioneAction";

const HomePage = () => {
    const dispatch = useDispatch();
    console.log(
        "Redux state posizione:",
        useSelector((state) => state.posizione)
    );
    const { myPosition, nearbyUsers, loading, maxKm, pageNumber, pageSize, totalElements, error } = useSelector((state) => state.location);

    const [musicianDataById, setMusicianDataById] = useState({});

    const fetchedIdsRef = useRef(new Set());

    useEffect(() => {
        dispatch(getMyLocation());
    }, [dispatch]);

    useEffect(() => {
        if (myPosition) {
            dispatch(
                getNearbyUsers({
                    lat: myPosition.lat,
                    lng: myPosition.lng,
                    maxKm,
                    pageNumber,
                    pageSize,
                    sortBy: "distance",
                })
            );
        }
    }, [dispatch, myPosition, maxKm, pageNumber, pageSize]);

    useEffect(() => {
        if (!nearbyUsers) return;

        nearbyUsers.forEach((pos) => {
            const id = pos.musicista_id;
            if (!fetchedIdsRef.current.has(id)) {
                fetchedIdsRef.current.add(id);
                (async () => {
                    try {
                        const res = await fetch(`http://localhost:3001/musician/${id}`);
                        if (!res.ok) throw new Error(`HTTP error ${res.status}`);
                        const data = await res.json();
                        setMusicianDataById((prev) => ({ ...prev, [id]: data.utente }));
                    } catch (err) {
                        console.error("Errore fetch musicista", id, err);
                    }
                })();
            }
        });
    }, [nearbyUsers]);

    const handleDistanceChange = (e) => {
        const value = Number(e.target.value);
        dispatch(setMaxKm(value));
        dispatch(setPageNumber(0));
    };

    const handlePrevPage = () => {
        if (pageNumber > 0) {
            dispatch(setPageNumber(pageNumber - 1));
        }
    };

    const handleNextPage = () => {
        if ((pageNumber + 1) * pageSize < totalElements) {
            dispatch(setPageNumber(pageNumber + 1));
        }
    };

    return (
        <div className="container mt-3">
            <div className="mb-3 d-flex align-items-center">
                <Form.Label className="me-2">Mostra utenti entro:</Form.Label>
                <Form.Control type="number" value={maxKm} onChange={handleDistanceChange} className="me-2" size="sm" />
                <span className="align-self-center">km</span>
            </div>

            {loading && <Spinner animation="border" />}

            {error && <Alert variant="danger">{error}</Alert>}

            {!loading && nearbyUsers?.length === 0 && <Alert variant="info">Nessun utente trovato entro {maxKm} km.</Alert>}

            <ListGroup className="mt-2">
                {nearbyUsers?.map((pos) => {
                    const user = musicianDataById[pos.musicista_id];
                    return (
                        <ListGroup.Item key={pos.musicista_id} className="d-flex justify-content-between align-items-center">
                            <div>{user ? `${user.username} (${user.nome})` : "Caricamento..."}</div>
                            <div>
                                {pos.distance != null && (
                                    <Badge bg="primary" pill>
                                        {pos.distance.toFixed(1)} km
                                    </Badge>
                                )}
                            </div>
                        </ListGroup.Item>
                    );
                })}
            </ListGroup>

            <div className="mt-3 d-flex justify-content-between">
                <Button variant="secondary" onClick={handlePrevPage} disabled={pageNumber === 0}>
                    Precedente
                </Button>
                <Button variant="secondary" onClick={handleNextPage} disabled={(pageNumber + 1) * pageSize >= totalElements}>
                    Successiva
                </Button>
            </div>
        </div>
    );
};

export default HomePage;
