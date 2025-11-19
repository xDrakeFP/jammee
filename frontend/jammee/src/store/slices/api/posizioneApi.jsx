import { apiRequest } from "./handleApi";

export const locationApi = {
    saveLocation: async (locationData) => {
        return await apiRequest("/location/me", {
            method: "POST",
            body: JSON.stringify(locationData),
        });
    },

    getMyLocation: async () => {
        return await apiRequest("/location/me", {
            method: "GET",
        });
    },

    deleteMyLocation: async () => {
        return await apiRequest("/location/me", {
            method: "DELETE",
        });
    },
};
