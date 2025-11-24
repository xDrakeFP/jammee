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

export const NEARBY_TYPES = {
    FETCH_MY_POSITION_REQUEST: "FETCH_MY_POSITION_REQUEST",
    FETCH_MY_POSITION_SUCCESS: "FETCH_MY_POSITION_SUCCESS",
    FETCH_MY_POSITION_FAILURE: "FETCH_MY_POSITION_FAILURE",

    FETCH_NEARBY_USERS_REQUEST: "FETCH_NEARBY_USERS_REQUEST",
    FETCH_NEARBY_USERS_SUCCESS: "FETCH_NEARBY_USERS_SUCCESS",
    FETCH_NEARBY_USERS_FAILURE: "FETCH_NEARBY_USERS_FAILURE",

    SET_MAX_KM: "SET_MAX_KM",
    SET_PAGE_NUMBER: "SET_PAGE_NUMBER",
};

export const getCurrentLocation = () => {
    return (dispatch) => {
        dispatch({ type: LOCATION_TYPES.GET_LOCATION_REQUEST });

        if (!navigator.geolocation) {
            const msg = "Geolocalizzazione non supportata dal browser";
            dispatch({ type: LOCATION_TYPES.GET_LOCATION_FAILURE, payload: msg });
            return Promise.reject(new Error(msg));
        }

        return new Promise((resolve, reject) => {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const locationData = {
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude,
                        accuracy: position.coords.accuracy,
                    };

                    dispatch({ type: LOCATION_TYPES.GET_LOCATION_SUCCESS, payload: locationData });
                    resolve(locationData);
                },
                (error) => {
                    let errorMessage = "Impossibile ottenere la posizione";
                    switch (error.code) {
                        case 1:
                            errorMessage = "Hai negato il permesso per accedere alla posizione";
                            break;
                        case 2:
                            errorMessage = "Informazioni sulla posizione non disponibili";
                            break;
                        case 3:
                            errorMessage = "Richiesta scaduta per ottenere la posizione";
                            break;
                    }
                    dispatch({ type: LOCATION_TYPES.GET_LOCATION_FAILURE, payload: errorMessage });
                    reject(new Error(errorMessage));
                },
                { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 }
            );
        });
    };
};

export const checkIfLocationSaved = () => {
    return async () => {
        try {
            const response = await locationApi.hasLocation();
            return response;
        } catch (err) {
            console.error("Errore checkIfLocationSaved:", err);
            return false;
        }
    };
};

export const saveLocation = (locationData) => {
    return async (dispatch) => {
        dispatch({ type: LOCATION_TYPES.SAVE_LOCATION_REQUEST });
        try {
            const body = {
                latitude: locationData.latitude,
                longitude: locationData.longitude,
                accuracy: locationData.accuracy,
            };

            const response = await locationApi.saveLocation(body);

            dispatch({ type: LOCATION_TYPES.SAVE_LOCATION_SUCCESS, payload: response });
            return response;
        } catch (err) {
            const msg = err?.message || "Errore nel salvataggio della posizione";
            dispatch({ type: LOCATION_TYPES.SAVE_LOCATION_FAILURE, payload: msg });
            throw err;
        }
    };
};

export const getMyLocation = () => {
    return async (dispatch) => {
        dispatch({ type: LOCATION_TYPES.GET_LOCATION_REQUEST });
        try {
            const response = await locationApi.getMyLocation();
            dispatch({ type: LOCATION_TYPES.GET_LOCATION_SUCCESS, payload: response });
            return response;
        } catch (err) {
            dispatch({ type: LOCATION_TYPES.GET_LOCATION_FAILURE, payload: err?.message || "Errore nel recupero della posizione" });
            throw err;
        }
    };
};

export const deleteMyLocation = () => {
    return async (dispatch) => {
        dispatch({ type: LOCATION_TYPES.SAVE_LOCATION_REQUEST });
        try {
            const response = await locationApi.deleteMyLocation();

            dispatch({ type: LOCATION_TYPES.SAVE_LOCATION_SUCCESS, payload: response });
            return response;
        } catch (err) {
            dispatch({ type: LOCATION_TYPES.SAVE_LOCATION_FAILURE, payload: err?.message || "Errore nella cancellazione della posizione" });
            throw err;
        }
    };
};

export const getNearbyUsers = ({ lat, lng, maxKm, pageNumber, pageSize, sortBy }) => {
    return async (dispatch) => {
        dispatch({ type: NEARBY_TYPES.FETCH_NEARBY_USERS_REQUEST });
        try {
            const response = await locationApi.getNearbyUsers({ lat, lng, maxKm, pageNumber, pageSize, sortBy });
            dispatch({ type: NEARBY_TYPES.FETCH_NEARBY_USERS_SUCCESS, payload: response });
            return response;
        } catch (err) {
            dispatch({ type: NEARBY_TYPES.FETCH_NEARBY_USERS_FAILURE, payload: err?.message || "Errore nel recupero degli utenti nelle vicinanze" });
            throw err;
        }
    };
};

export const setMaxKm = (maxKm) => {
    return (dispatch) => {
        localStorage.setItem("maxKm", maxKm);
        dispatch({ type: NEARBY_TYPES.SET_MAX_KM, payload: maxKm });
    };
};

export const setPageNumber = (pageNumber) => {
    return (dispatch) => {
        dispatch({ type: NEARBY_TYPES.SET_PAGE_NUMBER, payload: pageNumber });
    };
};
