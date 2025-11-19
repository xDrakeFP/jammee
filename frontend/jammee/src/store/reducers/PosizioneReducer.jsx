import { LOCATION_TYPES } from "../actions/PosizioneAction";

const initialState = {
    currentLocation: null,

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
                loadiung: false,
                currentLocation: action.payload,
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
        default:
            return state;
    }
};

export default posizioneReducer;
