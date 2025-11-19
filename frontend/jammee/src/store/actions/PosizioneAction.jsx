import { locationApi } from "../slices/api/posizioneApi";

export const LOCATION_TYPES = {
    GET_LOCATION_REQUEST: "GET_LOCATION_REQUEST",
    GET_LOCATION_SUCCESS: "GET_LOCATION_SUCCESS",
    GET_LOCATION_FAILURE: "GET_LOCATION_FAILURE",

    SAVE_LOCATION_REQUEST: "SAVE_LOCATION_REQUEST",
    SAVE_LOCATION_SUCCESS: "SAVE_LOCATION_SUCCESS",
    SAVE_LOCATION_FAILURE: "SAVE_LOCATION_FAILURE",

    CLEAR_LOCATION_ERROR: "CLEAR_LOCATION_ERROR",
    CLEAR_LOCATION_DATA: "CLEAR_LOCATION_DATA",
};

export const getCurrentLocation = () => {
    return (dispatch) => {
        dispatch({ type: LOCATION_TYPES.GET_LOCATION_REQUEST });

        if (!navigator.geolocation) {
            dispatch({
                type: LOCATION_TYPES.GET_LOCATION_FAILURE,
                payload: "Geolocalizzazione non supportata dal browser",
            });
            return;
        }

        navigator.geolocation.getCurrentPosition(
            (position) => {
                const locationData = {
                    latitude: position.coords.latitude,
                    longitude: position.coords.longitude,
                    accuracy: position.coords.accuracy,
                };

                console.log("Posizione salvata:", locationData);

                dispatch({
                    type: LOCATION_TYPES.GET_LOCATION_SUCCESS,
                    payload: locationData,
                });

                dispatch(saveLocation(locationData));
            },

            (error) => {
                let errorMessage = "Impossibile ottenere la posizione";

                switch (error.code) {
                    case error.PERMISSION_DENIED:
                        errorMessage = "Hai negato il permesso per accedere alla posizione";
                        break;
                    case error.POSITION_UNAVAILABLE:
                        errorMessage = "Informazioni sulla posizione non disponibili";
                        break;
                    case error.TIMEOUT:
                        errorMessage = "Richiesta scaduta per ottenere la posizione";
                        break;
                    default:
                        errorMessage = "Si è verificato un errore sconosciuto";
                }

                console.error("Errore geolocalizzazione:", errorMessage);

                dispatch({
                    type: LOCATION_TYPES.GET_LOCATION_FAILURE,
                    payload: errorMessage,
                });
            },
            { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 }
        );
    };
};

export const saveLocation = (locationData) => {
    return async (dispatch) => {
        dispatch({ type: LOCATION_TYPES.SAVE_LOCATION_REQUEST });

        try {
            const response = await locationApi.saveLocation(locationData);

            console.log("Posizione salvata con successo", response);

            dispatch({
                type: LOCATION_TYPES.SAVE_LOCATION_SUCCESS,
                payload: response,
            });

            return response;
        } catch (error) {
            console.error("Errore nel salvataggio della posizione:", error);
            dispatch({
                type: LOCATION_TYPES.SAVE_LOCATION_FAILURE,
                payload: error.message || "Errore nel salvataggio della posizione",
            });
            throw error;
        }
    };
};
