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

    hasLocation: async () => {
        return await apiRequest("/location/has-position", {
            method: "GET",
        });
    },

    deleteMyLocation: async () => {
        return await apiRequest("/location/me", {
            method: "DELETE",
        });
    },

    getNearbyUsers: async ({ lat, lng, maxKm, pageNumber, pageSize, sortBy }) => {
        return await apiRequest(`/location/nearby?lat=${lat}&lng=${lng}&maxKm=${maxKm}&page=${pageNumber}&page=${pageSize}&sort=${sortBy}`);
    },
};
