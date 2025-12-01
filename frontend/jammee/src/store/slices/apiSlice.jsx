import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const apiSlice = createApi({
    reducerPath: "api",
    baseQuery: fetchBaseQuery({
        baseUrl: "http://localhost:3001",
        prepareHeaders: (headers, { getState }) => {
            const token = getState().auth.token;

            if (token) {
                headers.set("Authorization", `Bearer ${token}`);
            }
            return headers;
        },
    }),
    tagTypes: ["Utente", "Musicista", "Strumento", "JamSession", "Genere", "Messaggio", "Feedback", "Auth", "Competenza", "Dimestichezza", "Posizione"],
    endpoints: () => ({}),
});
