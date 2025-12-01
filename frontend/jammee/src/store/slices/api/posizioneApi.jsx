import { apiSlice } from "../apiSlice";
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
};

export const posizioniApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        getNearby: builder.query({
            query: ({ lat, lng, maxKm, pageNumber = 0, pageSize = 10, strumentoId, genereId }) => {
                const params = new URLSearchParams();
                if (lat != null) params.append("lat", lat);
                if (lng != null) params.append("lng", lng);
                params.append("maxKm", String(maxKm));
                params.append("pageNumber", String(pageNumber));
                params.append("pageSize", String(pageSize));
                if (strumentoId) params.append("strumentoId", strumentoId);
                if (genereId) params.append("genereId", genereId);

                return `/location/search?${params.toString()}`;
            },
            providesTags: (result) =>
                result?.content ? [...result.content.map((p) => ({ type: "Posizione", id: p.posizione.id })), { type: "Posizione", id: "LIST" }] : [{ type: "Posizione", id: "LIST" }],
        }),
    }),
});

export const { useGetNearbyQuery } = posizioniApi;
