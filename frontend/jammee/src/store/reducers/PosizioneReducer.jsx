import { LOCATION_TYPES, NEARBY_TYPES } from "../actions/PosizioneAction";

const initialState = {
    myPosition: null,

    nearbyUsers: [],
    totalElements: 0,
    pageNumber: 0,
    pageSize: 10,
    maxKm: Number(localStorage.getItem("maxKm") || 50),

    loading: false,
    saving: false,
    error: null,
};

const posizioneReducer = (state = initialState, action) => {
    switch (action.type) {
        case LOCATION_TYPES.GET_LOCATION_REQUEST:
            return {
                ...state,
                loading: true,
                error: null,
            };
        case LOCATION_TYPES.GET_LOCATION_SUCCESS:
            return {
                ...state,
                loading: false,
                myPosition: action.payload,
                error: null,
            };
        case LOCATION_TYPES.GET_LOCATION_FAILURE:
            return {
                ...state,
                loading: false,
                error: action.payload,
            };
        case LOCATION_TYPES.SAVE_LOCATION_REQUEST:
            return {
                ...state,
                saving: true,
                error: null,
            };
        case LOCATION_TYPES.SAVE_LOCATION_SUCCESS:
            return {
                ...state,
                saving: false,
                error: null,
            };
        case LOCATION_TYPES.SAVE_LOCATION_FAILURE:
            return {
                ...state,
                saving: false,
                error: action.payload,
            };

        case NEARBY_TYPES.FETCH_NEARBY_USERS_REQUEST:
            return {
                ...state,
                loading: true,
                error: null,
            };
        case NEARBY_TYPES.FETCH_NEARBY_USERS_SUCCESS:
            return {
                ...state,
                loading: false,
                nearbyUsers: action.payload?.content ?? action.payload ?? [],
                totalElements: action.payload?.totalElements ?? action.payload?.length ?? 0,
                error: null,
            };
        case NEARBY_TYPES.FETCH_NEARBY_USERS_FAILURE:
            return {
                ...state,
                loading: false,
                error: action.payload,
            };

        case NEARBY_TYPES.SET_MAX_KM:
            return {
                ...state,
                maxKm: action.payload,
            };
        case NEARBY_TYPES.SET_PAGE_NUMBER:
            return {
                ...state,
                pageNumber: action.payload,
            };

        default:
            return state;
    }
};

export default posizioneReducer;
