import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import Button from "react-bootstrap/Button";
import Spinner from "react-bootstrap/Spinner";
import Modal from "react-bootstrap/Modal";
import { getCurrentLocation, saveLocation, checkIfLocationSaved, deleteMyLocation } from "../../../store/actions/PosizioneAction";

const SaveLocationButton = () => {
    const dispatch = useDispatch();
    const { id: viewingUserId } = useParams();
    const currentUserId = useSelector((state) => state.auth?.user?.id);
    const isOwnProfile = !viewingUserId || String(viewingUserId) === String(currentUserId);

    const [alreadySaved, setAlreadySaved] = useState(false);
    const [show, setShow] = useState(false);
    const [loc, setLoc] = useState(null);
    const [busy, setBusy] = useState(false);
    const [error, setError] = useState("");

    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        dispatch(checkIfLocationSaved()).then(setAlreadySaved);
    }, [dispatch]);

    const openAndFetch = async () => {
        if (!isOwnProfile) return;

        setError("");
        setBusy(true);

        try {
            const locationData = await dispatch(getCurrentLocation());
            setLoc(locationData);
            setShow(true);
        } catch (err) {
            setError(err.message || "Errore ottenendo posizione");
        } finally {
            setBusy(false);
        }
    };

    const handleConfirm = async () => {
        if (!loc) return;

        setBusy(true);
        setError("");

        try {
            await dispatch(saveLocation(loc));
            setAlreadySaved(true);
            setShow(false);
            setLoc(null);
        } catch (err) {
            setError(err.message || "Errore nel salvataggio");
        } finally {
            setBusy(false);
        }
    };

    const handleDeleteModal = () => {
        setShowDeleteModal(true);
    };

    const handleConfirmDelete = async () => {
        setBusy(true);
        try {
            await dispatch(deleteMyLocation());
            setAlreadySaved(false);
            setShowDeleteModal(false);
        } catch (err) {
            setErrorMessage(err.message || "Errore nella cancellazione della posizione");
            console.error("Errore nella cancellazione della posizione:", errorMessage);
        } finally {
            setBusy(false);
        }
    };

    let variant = "secondary";
    let text = "Salva posizione";

    if (alreadySaved) {
        variant = "primary";
        text = "Posizione salvata";
    }

    return (
        <>
            {isOwnProfile && (
                <>
                    <Button variant={variant} disabled={busy || alreadySaved} onClick={openAndFetch}>
                        {busy ? (
                            <>
                                <Spinner as="span" animation="border" size="sm" className="me-2" />
                                Attendi...
                            </>
                        ) : (
                            text
                        )}
                    </Button>

                    {alreadySaved && (
                        <Button
                            className="ms-2"
                            variant="danger"
                            onClick={handleDeleteModal}
                            disabled={!isOwnProfile || !alreadySaved || busy}
                            title={alreadySaved ? "Elimina la tua posizione salvata" : "Nessuna posizione salvata"}
                        >
                            <i className="bi bi-trash"></i>
                        </Button>
                    )}
                </>
            )}

            {error && (
                <div className="mt-2">
                    <small className="text-danger">{error}</small>
                </div>
            )}

            <Modal show={show} onHide={() => setShow(false)} centered size="sm">
                <Modal.Body className="text-center">
                    <p>Vuoi salvare la posizione?</p>
                </Modal.Body>

                <Modal.Footer className="d-flex justify-content-between">
                    <Button variant="secondary" onClick={() => setShow(false)} disabled={busy}>
                        Annulla
                    </Button>

                    <Button variant="primary" onClick={handleConfirm} disabled={busy}>
                        {busy ? (
                            <>
                                <Spinner as="span" animation="border" size="sm" className="me-2" />
                                Salvo...
                            </>
                        ) : (
                            "Conferma"
                        )}
                    </Button>
                </Modal.Footer>
            </Modal>

            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)} centered size="sm">
                <Modal.Body className="text-center">Sei sicuro di voler eliminare la posizione salvata?</Modal.Body>
                <Modal.Footer className="d-flex justify-content-between">
                    <Button variant="secondary" onClick={() => setShowDeleteModal(false)} disabled={busy}>
                        Annulla
                    </Button>
                    <Button variant="danger" onClick={handleConfirmDelete} disabled={busy}>
                        {busy ? "Elimino..." : "Elimina"}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default SaveLocationButton;
