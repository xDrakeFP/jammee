import { apiSlice } from "../apiSlice";

export const musicistaApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        getMusicians: builder.query({
            query: () => ({
                url: "/musician/me",
                method: "GET",
            }),
            providesTags: ["Musicians"],
        }),
    }),
});

export const { useGetMusiciansQuery } = musicistaApi;
