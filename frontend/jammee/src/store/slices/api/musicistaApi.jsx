import { apiSlice } from "../apiSlice";

export const musicistaApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        getMusicianById: builder.query({
            query: (id) => ({
                url: `/musician/${id}`,
                method: "GET",
            }),
            providesTags: () => [{ type: "Musicista" }],
        }),

        getMusicianMe: builder.query({
            query: () => ({
                url: "/musician/me",
                method: "GET",
            }),
            providesTags: () => [{ type: "Musicista" }],
        }),
    }),
});

export const { useGetMusicianMeQuery, useGetMusicianByIdQuery } = musicistaApi;
