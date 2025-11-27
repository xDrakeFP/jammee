import { apiSlice } from "../apiSlice";

export const generiApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        getAllGeneri: builder.query({
            query: () => ({
                url: `/genres`,
                method: "GET",
            }),
            providesTags: () => [{ type: "Genere" }],
        }),
    }),
});

export const { useGetAllGeneriQuery } = generiApi;
