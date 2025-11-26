import { apiSlice } from "../apiSlice";

export const strumentiApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        getAllStrumenti: builder.query({
            query: () => ({
                url: `/instruments`,
                method: "GET",
            }),
            providesTags: () => [{ type: "Strumento" }],
        }),
    }),
});

export const { useGetAllStrumentiQuery } = strumentiApi;
