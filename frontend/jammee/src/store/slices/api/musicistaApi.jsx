import { apiSlice } from "../apiSlice";

export const musicistaApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        getMusicianById: builder.query({
            query: (id) => ({
                url: `/musician/${id}`,
                method: "GET",
            }),
            providesTags: () => [{ type: "Musicians" }],
        }),

        getMusicianMe: builder.query({
            query: () => ({
                url: "/musician/me",
                method: "GET",
            }),
            providesTags: ["Musicians"],
        }),
    }),
});

export const { useGetMusicianMeQuery, useGetMusicianByIdQuery } = musicistaApi;
